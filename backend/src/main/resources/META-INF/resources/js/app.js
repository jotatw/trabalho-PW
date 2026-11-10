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

        if (response.status === 401) {
            loginMessage.textContent = "E-mail ou senha inválidos.";
            return;
        }

        if (response.status === 403) {
            loginMessage.textContent =
                "Usuário inativo ou sem perfil autorizado.";
            return;
        }

        if (!response.ok) {
            loginMessage.textContent =
                "Não foi possível realizar o login.";
            return;
        }

        const resposta = await response.json();

        if (!resposta.token || !resposta.usuario) {
            loginMessage.textContent =
                "Resposta de autenticação inválida.";
            return;
        }

        const usuario = resposta.usuario;

        // Armazena o JWT e os dados do usuário.
        sessionStorage.setItem("token", resposta.token);
        sessionStorage.setItem("usuario", JSON.stringify(usuario));

        loginMessage.textContent = `Bem-vindo, ${usuario.nome}!`;

        window.location.href = "/dashboard.html";

    } catch (error) {
        console.error("Erro no login:", error);

        loginMessage.textContent =
            "Não foi possível conectar ao servidor.";
    }
});
