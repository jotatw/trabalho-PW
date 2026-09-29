function carregarLayout(nomePagina) {

    const app = document.getElementById("app-layout");

    if (!app) {
        return;
    }

    const sidebar = `
        <aside class="sidebar">

            <div class="sidebar-header">
                <h1>Locadora</h1>
            </div>

            <nav class="menu">

                <a href="/dashboard.html"
                   class="menu-item ${nomePagina === "dashboard" ? "active" : ""}">
                    Dashboard
                </a>

                <a href="/pages/clientes.html"
                   class="menu-item ${nomePagina === "clientes" ? "active" : ""}">
                    Clientes
                </a>

                <a href="/pages/veiculos.html"
                   class="menu-item ${nomePagina === "veiculos" ? "active" : ""}">
                    Veículos
                </a>

                <a href="/pages/reservas.html"
                   class="menu-item ${nomePagina === "reservas" ? "active" : ""}">
                    Reservas
                </a>

                <a href="/pages/locacoes.html"
                   class="menu-item ${nomePagina === "locacoes" ? "active" : ""}">
                    Locações
                </a>

                <a href="/pages/usuarios.html"
                   class="menu-item ${nomePagina === "usuarios" ? "active" : ""}">
                    Usuários
                </a>

                <a href="/pages/relatorios.html"
                   class="menu-item ${nomePagina === "relatorios" ? "active" : ""}">
                    Relatórios
                </a>

            </nav>

            <div class="sidebar-footer">

                <a href="/pages/administracao.html"
                   class="menu-item ${nomePagina === "administracao" ? "active" : ""}">
                    Administração
                </a>

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

        window.location.href = "/";
    });
}


function carregarUsuario() {

    const usuarioJSON = sessionStorage.getItem("usuario");

    if (!usuarioJSON) {

        window.location.href = "/";

        return;
    }

    const usuario = JSON.parse(usuarioJSON);

    const userName = document.getElementById("user-name");

    if (userName) {
        userName.textContent = usuario.nome;
    }
}