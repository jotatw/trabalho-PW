const loginForm = document.getElementById("login-form");
const loginMessage = document.getElementById("login-message");

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    loginMessage.textContent = "Entrando...";

    try {
        const response = await fetch("/usuarios/login", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                senha: senha
            })
        });

        if (!response.ok) {
            loginMessage.textContent = "E-mail ou senha inválidos.";
            return;
        }

        const usuario = await response.json();

        loginMessage.textContent = `Bem-vindo, ${usuario.nome}!`;

        console.log("Usuário autenticado:", usuario);

        sessionStorage.setItem("usuario", JSON.stringify(usuario));

        window.location.href = "/dashboard.html";

    } catch (error) {
        console.error(error);

        loginMessage.textContent =
            "Não foi possível conectar ao servidor.";
    }
});