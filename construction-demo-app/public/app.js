const state = {
  token: localStorage.getItem("demo_token"),
  user: null,
  selectedProjectId: 101
};

const elements = {
  loginPanel: document.querySelector("[data-testid='login-panel']"),
  dashboardPanel: document.querySelector("[data-testid='dashboard-panel']"),
  username: document.querySelector("[data-testid='username-input']"),
  password: document.querySelector("[data-testid='password-input']"),
  loginButton: document.querySelector("[data-testid='login-button']"),
  loginError: document.querySelector("[data-testid='login-error']"),
  logoutButton: document.querySelector("[data-testid='logout-button']"),
  currentUser: document.querySelector("[data-testid='current-user']"),
  projectsList: document.querySelector("[data-testid='projects-list']"),
  documentsList: document.querySelector("[data-testid='documents-list']"),
  documentTitle: document.querySelector("[data-testid='document-title-input']"),
  documentType: document.querySelector("[data-testid='document-type-select']"),
  createDocumentButton: document.querySelector("[data-testid='create-document-button']"),
  documentError: document.querySelector("[data-testid='document-error']"),
  documentSuccess: document.querySelector("[data-testid='document-success']"),
  adminPanel: document.querySelector("[data-testid='admin-panel']"),
  loadUsersButton: document.querySelector("[data-testid='load-users-button']"),
  usersList: document.querySelector("[data-testid='users-list']")
};

async function api(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(state.token ? { Authorization: `Bearer ${state.token}` } : {}),
      ...(options.headers || {})
    }
  });
  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.error || `Request failed with status ${response.status}`);
  }
  return data;
}

function showDashboard() {
  elements.loginPanel.classList.add("hidden");
  elements.dashboardPanel.classList.remove("hidden");
}

function showLogin() {
  elements.dashboardPanel.classList.add("hidden");
  elements.loginPanel.classList.remove("hidden");
}

function renderProjects(projects) {
  elements.projectsList.innerHTML = projects.map(project => `
    <article class="item" data-testid="project-card" data-project-id="${project.id}">
      <strong>${project.name}</strong>
      <span>Budget: ${project.budget.toLocaleString("en-US")}</span>
      <span class="badge">${project.status}</span>
    </article>
  `).join("");

  document.querySelectorAll("[data-testid='project-card']").forEach(card => {
    card.addEventListener("click", async () => {
      state.selectedProjectId = Number(card.dataset.projectId);
      await loadDocuments();
    });
  });
}

function renderDocuments(documents) {
  elements.documentsList.innerHTML = documents.map(document => `
    <article class="item" data-testid="document-card" data-document-id="${document.id}">
      <strong>${document.title}</strong>
      <span>${document.type}</span>
      <span class="badge">${document.status}</span>
    </article>
  `).join("");
}

function renderUsers(users) {
  elements.usersList.innerHTML = users.map(user => `
    <article class="item" data-testid="user-card">
      <strong>${user.name}</strong>
      <span>${user.username}</span>
      <span class="badge">${user.role}</span>
    </article>
  `).join("");
}

async function loadDashboard() {
  const me = await api("/api/me");
  state.user = me.user;
  elements.currentUser.textContent = `${state.user.name} / ${state.user.role}`;
  elements.adminPanel.classList.toggle("hidden", state.user.role !== "ADMIN");

  const projectsData = await api("/api/projects");
  renderProjects(projectsData.projects);

  state.selectedProjectId = projectsData.projects[0]?.id || 101;
  await loadDocuments();
  showDashboard();
}

async function loadDocuments() {
  const documentsData = await api(`/api/documents?projectId=${state.selectedProjectId}`);
  renderDocuments(documentsData.documents);
}

elements.loginButton.addEventListener("click", async () => {
  elements.loginError.textContent = "";
  try {
    const data = await api("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({
        username: elements.username.value,
        password: elements.password.value
      })
    });
    state.token = data.token;
    localStorage.setItem("demo_token", state.token);
    await loadDashboard();
  } catch (error) {
    elements.loginError.textContent = error.message;
  }
});

elements.logoutButton.addEventListener("click", () => {
  state.token = null;
  state.user = null;
  localStorage.removeItem("demo_token");
  showLogin();
});

elements.createDocumentButton.addEventListener("click", async () => {
  elements.documentError.textContent = "";
  elements.documentSuccess.textContent = "";
  try {
    const data = await api("/api/documents", {
      method: "POST",
      body: JSON.stringify({
        projectId: state.selectedProjectId,
        title: elements.documentTitle.value,
        type: elements.documentType.value
      })
    });
    elements.documentSuccess.textContent = `Created document #${data.document.id}`;
    elements.documentTitle.value = "";
    await loadDocuments();
  } catch (error) {
    elements.documentError.textContent = error.message;
  }
});

elements.loadUsersButton.addEventListener("click", async () => {
  const data = await api("/api/admin/users");
  renderUsers(data.users);
});

if (state.token) {
  loadDashboard().catch(() => {
    localStorage.removeItem("demo_token");
    state.token = null;
    showLogin();
  });
}
