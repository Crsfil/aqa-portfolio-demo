const http = require("http");
const fs = require("fs");
const path = require("path");
const crypto = require("crypto");

const PORT = Number(process.env.PORT || 3000);
const PUBLIC_DIR = path.join(__dirname, "..", "public");

const users = [
  { id: 1, username: "admin", password: "admin123", name: "Admin User", role: "ADMIN" },
  { id: 2, username: "manager", password: "manager123", name: "Project Manager", role: "MANAGER" },
  { id: 3, username: "viewer", password: "viewer123", name: "Read Only User", role: "VIEWER" }
];

let projects = [
  { id: 101, name: "Residential Complex North", budget: 12500000, status: "ACTIVE", ownerId: 2 },
  { id: 102, name: "Warehouse Reconstruction", budget: 8400000, status: "PLANNED", ownerId: 2 }
];

let documents = [
  { id: 501, projectId: 101, title: "Foundation estimate", type: "ESTIMATE", status: "APPROVED", ownerId: 2 },
  { id: 502, projectId: 101, title: "Concrete works act", type: "ACT", status: "DRAFT", ownerId: 2 },
  { id: 503, projectId: 102, title: "Safety checklist", type: "CHECKLIST", status: "REVIEW", ownerId: 2 }
];

const sessions = new Map();

function sendJson(res, status, data) {
  const body = JSON.stringify(data);
  res.writeHead(status, {
    "Content-Type": "application/json; charset=utf-8",
    "Content-Length": Buffer.byteLength(body)
  });
  res.end(body);
}

function sendText(res, status, text, contentType = "text/plain; charset=utf-8") {
  res.writeHead(status, { "Content-Type": contentType });
  res.end(text);
}

function readBody(req) {
  return new Promise((resolve, reject) => {
    let raw = "";
    req.on("data", chunk => {
      raw += chunk;
      if (raw.length > 1_000_000) {
        reject(new Error("Request body is too large"));
        req.destroy();
      }
    });
    req.on("end", () => {
      if (!raw) {
        resolve({});
        return;
      }
      try {
        resolve(JSON.parse(raw));
      } catch (error) {
        reject(new Error("Invalid JSON body"));
      }
    });
  });
}

function publicUser(user) {
  return { id: user.id, username: user.username, name: user.name, role: user.role };
}

function getAuthUser(req) {
  const header = req.headers.authorization || "";
  const token = header.startsWith("Bearer ") ? header.slice("Bearer ".length) : "";
  const userId = sessions.get(token);
  return users.find(user => user.id === userId) || null;
}

function requireAuth(req, res) {
  const user = getAuthUser(req);
  if (!user) {
    sendJson(res, 401, { error: "Unauthorized" });
    return null;
  }
  return user;
}

function requireRole(user, res, allowedRoles) {
  if (!allowedRoles.includes(user.role)) {
    sendJson(res, 403, { error: "Forbidden" });
    return false;
  }
  return true;
}

function matchRoute(req, method, pattern) {
  if (req.method !== method) {
    return null;
  }
  const url = new URL(req.url, `http://${req.headers.host}`);
  const routeParts = pattern.split("/").filter(Boolean);
  const pathParts = url.pathname.split("/").filter(Boolean);
  if (routeParts.length !== pathParts.length) {
    return null;
  }
  const params = {};
  for (let i = 0; i < routeParts.length; i += 1) {
    const routePart = routeParts[i];
    const pathPart = pathParts[i];
    if (routePart.startsWith(":")) {
      params[routePart.slice(1)] = pathPart;
    } else if (routePart !== pathPart) {
      return null;
    }
  }
  return { params, query: url.searchParams };
}

function serveStatic(req, res) {
  const url = new URL(req.url, `http://${req.headers.host}`);
  const requestedPath = url.pathname === "/" ? "/index.html" : url.pathname;
  const safePath = path.normalize(requestedPath).replace(/^(\.\.[/\\])+/, "");
  const filePath = path.join(PUBLIC_DIR, safePath);

  if (!filePath.startsWith(PUBLIC_DIR)) {
    sendText(res, 403, "Forbidden");
    return;
  }

  fs.readFile(filePath, (error, data) => {
    if (error) {
      sendText(res, 404, "Not found");
      return;
    }
    const ext = path.extname(filePath);
    const contentTypes = {
      ".html": "text/html; charset=utf-8",
      ".css": "text/css; charset=utf-8",
      ".js": "application/javascript; charset=utf-8"
    };
    res.writeHead(200, { "Content-Type": contentTypes[ext] || "application/octet-stream" });
    res.end(data);
  });
}

async function handleApi(req, res) {
  const healthRoute = matchRoute(req, "GET", "/api/health");
  if (healthRoute) {
    sendJson(res, 200, { status: "UP", app: "construction-demo-app" });
    return;
  }

  const loginRoute = matchRoute(req, "POST", "/api/auth/login");
  if (loginRoute) {
    const body = await readBody(req);
    const user = users.find(item => item.username === body.username && item.password === body.password);
    if (!user) {
      sendJson(res, 401, { error: "Invalid username or password" });
      return;
    }
    const token = crypto.randomBytes(24).toString("hex");
    sessions.set(token, user.id);
    sendJson(res, 200, { token, user: publicUser(user) });
    return;
  }

  const meRoute = matchRoute(req, "GET", "/api/me");
  if (meRoute) {
    const user = requireAuth(req, res);
    if (!user) return;
    sendJson(res, 200, { user: publicUser(user) });
    return;
  }

  const usersRoute = matchRoute(req, "GET", "/api/admin/users");
  if (usersRoute) {
    const user = requireAuth(req, res);
    if (!user || !requireRole(user, res, ["ADMIN"])) return;
    sendJson(res, 200, { users: users.map(publicUser) });
    return;
  }

  const projectsListRoute = matchRoute(req, "GET", "/api/projects");
  if (projectsListRoute) {
    const user = requireAuth(req, res);
    if (!user) return;
    sendJson(res, 200, { projects });
    return;
  }

  const createProjectRoute = matchRoute(req, "POST", "/api/projects");
  if (createProjectRoute) {
    const user = requireAuth(req, res);
    if (!user || !requireRole(user, res, ["ADMIN", "MANAGER"])) return;
    const body = await readBody(req);
    if (!body.name || body.name.length < 3) {
      sendJson(res, 422, { error: "Project name must contain at least 3 characters" });
      return;
    }
    const project = {
      id: Date.now(),
      name: body.name,
      budget: Number(body.budget || 0),
      status: body.status || "PLANNED",
      ownerId: user.id
    };
    projects.push(project);
    sendJson(res, 201, { project });
    return;
  }

  const projectRoute = matchRoute(req, "GET", "/api/projects/:id");
  if (projectRoute) {
    const user = requireAuth(req, res);
    if (!user) return;
    const project = projects.find(item => item.id === Number(projectRoute.params.id));
    if (!project) {
      sendJson(res, 404, { error: "Project not found" });
      return;
    }
    sendJson(res, 200, { project });
    return;
  }

  const documentsListRoute = matchRoute(req, "GET", "/api/documents");
  if (documentsListRoute) {
    const user = requireAuth(req, res);
    if (!user) return;
    const projectId = documentsListRoute.query.get("projectId");
    const filtered = projectId
      ? documents.filter(item => item.projectId === Number(projectId))
      : documents;
    sendJson(res, 200, { documents: filtered });
    return;
  }

  const createDocumentRoute = matchRoute(req, "POST", "/api/documents");
  if (createDocumentRoute) {
    const user = requireAuth(req, res);
    if (!user || !requireRole(user, res, ["ADMIN", "MANAGER"])) return;
    const body = await readBody(req);
    const project = projects.find(item => item.id === Number(body.projectId));
    if (!project) {
      sendJson(res, 404, { error: "Project not found" });
      return;
    }
    if (!body.title || body.title.length < 3) {
      sendJson(res, 422, { error: "Document title must contain at least 3 characters" });
      return;
    }
    const document = {
      id: Date.now(),
      projectId: project.id,
      title: body.title,
      type: body.type || "ACT",
      status: "DRAFT",
      ownerId: user.id
    };
    documents.push(document);
    sendJson(res, 201, { document });
    return;
  }

  const updateDocumentStatusRoute = matchRoute(req, "PATCH", "/api/documents/:id/status");
  if (updateDocumentStatusRoute) {
    const user = requireAuth(req, res);
    if (!user || !requireRole(user, res, ["ADMIN", "MANAGER"])) return;
    const document = documents.find(item => item.id === Number(updateDocumentStatusRoute.params.id));
    if (!document) {
      sendJson(res, 404, { error: "Document not found" });
      return;
    }
    const body = await readBody(req);
    const allowedStatuses = ["DRAFT", "REVIEW", "APPROVED", "REJECTED"];
    if (!allowedStatuses.includes(body.status)) {
      sendJson(res, 422, { error: "Unsupported document status" });
      return;
    }
    document.status = body.status;
    sendJson(res, 200, { document });
    return;
  }

  sendJson(res, 404, { error: "API route not found" });
}

const server = http.createServer(async (req, res) => {
  try {
    if (req.url.startsWith("/api/")) {
      await handleApi(req, res);
      return;
    }
    serveStatic(req, res);
  } catch (error) {
    sendJson(res, 400, { error: error.message });
  }
});

server.listen(PORT, () => {
  console.log(`Construction demo app is running at http://localhost:${PORT}`);
});
