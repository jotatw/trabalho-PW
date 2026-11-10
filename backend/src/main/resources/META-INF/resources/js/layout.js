function carregarLayout(nomePagina) {
    const app = document.getElementById("app-layout");

    if (!app) {
        return;
    }

    const token = sessionStorage.getItem("token");
    const usuarioJSON = sessionStorage.getItem("usuario");

    if (!token || !usuarioJSON) {
        window.location.href = "/";
        return;
    }

    const usuario = JSON.parse(usuarioJSON);
    const perfil = (usuario.perfil || "").trim().toUpperCase();
    const ehAdmin = perfil === "ADMIN";

    const menuBase = [
        { pagina: "dashboard", texto: "Visão geral", url: "/dashboard.html" },
        { pagina: "clientes", texto: "Clientes", url: "/pages/clientes.html" },
        { pagina: "veiculos", texto: "Veículos", url: "/pages/veiculos.html" },
        { pagina: "reservas", texto: "Reservas", url: "/pages/reservas.html" },
        { pagina: "locacoes", texto: "Locações", url: "/pages/locacoes.html" }
    ];

    if (ehAdmin) {
        menuBase.push(
            { pagina: "usuarios", texto: "Usuários", url: "/pages/usuarios.html" },
            { pagina: "relatorios", texto: "Relatórios", url: "/pages/relatorios.html" }
        );
    }

    const itensMenu = menuBase.map(item => `
        <a href="${item.url}"
           class="menu-item ${nomePagina === item.pagina ? "active" : ""}">
            ${item.texto}
        </a>
    `).join("");

    const menuAdministracao = ehAdmin ? `
        <a href="/pages/administracao.html"
           class="menu-item ${nomePagina === "administracao" ? "active" : ""}">
            Administração
        </a>
    ` : "";

    const sidebar = `
        <aside class="sidebar">
            <div class="sidebar-header">
                <h1>Locadora</h1>
            </div>

            <nav class="menu">
                ${itensMenu}
            </nav>

            <div class="sidebar-footer">
                ${menuAdministracao}

                <button id="logout-button">
                    Sair
                </button>
            </div>
        </aside>
    `;

    app.insertAdjacentHTML("afterbegin", sidebar);

    configurarLogout();
    carregarUsuario();
}

function configurarLogout() {
    const logoutButton = document.getElementById("logout-button");

    if (!logoutButton) {
        return;
    }

    logoutButton.addEventListener("click", () => {
        sessionStorage.removeItem("usuario");
        sessionStorage.removeItem("token");

        window.location.href = "/";
    });
}

function carregarUsuario() {
    const token = sessionStorage.getItem("token");
    const usuarioJSON = sessionStorage.getItem("usuario");

    if (!token || !usuarioJSON) {
        window.location.href = "/";
        return;
    }

    const usuario = JSON.parse(usuarioJSON);

    const userName = document.getElementById("user-name");

    if (userName) {
        userName.textContent = usuario.nome;
    }
}
