const usuario = JSON.parse(usuarioJSON);

const userName = document.getElementById("user-name");

userName.textContent = usuario.nome;

const logoutButton = document.getElementById("logout-button");

logoutButton.addEventListener("click", () => {
    sessionStorage.removeItem("usuario");

    window.location.href = "/";
});