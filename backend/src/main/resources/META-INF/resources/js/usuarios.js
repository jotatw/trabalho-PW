async function carregarUsuarios() {

    const lista = document.getElementById("usuarios-lista");

    try {

        const response = await fetch("/usuarios");

        if (!response.ok) {
            throw new Error("Erro ao buscar usuários.");
        }

        const usuarios = await response.json();

        lista.innerHTML = "";

        if (usuarios.length === 0) {

            lista.innerHTML = `
                <tr>
                    <td colspan="5">
                        Nenhum usuário cadastrado.
                    </td>
                </tr>
            `;

            return;
        }

        usuarios.forEach(usuario => {

            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${usuario.id}</td>
                <td>${usuario.nome}</td>
                <td>${usuario.email}</td>
                <td>${usuario.ativo ? "Ativo" : "Inativo"}</td>
                <td>
                    <button type="button">
                        Editar
                    </button>
                </td>
            `;

            lista.appendChild(linha);
        });

    } catch (error) {

        console.error(error);

        lista.innerHTML = `
            <tr>
                <td colspan="5">
                    Não foi possível carregar os usuários.
                </td>
            </tr>
        `;
    }
}


function configurarFormularioUsuario() {

    const abrirButton =
        document.getElementById("novo-usuario-button");

    const cancelarButton =
        document.getElementById("cancelar-usuario-button");

    const container =
        document.getElementById("usuario-form-container");

    const form =
        document.getElementById("usuario-form");


    abrirButton.addEventListener("click", () => {

        container.classList.remove("hidden");

    });


    cancelarButton.addEventListener("click", () => {

        container.classList.add("hidden");

        form.reset();

        limparMensagem();

    });


    form.addEventListener("submit", async (event) => {

        event.preventDefault();

        await cadastrarUsuario();

    });
}


async function cadastrarUsuario() {

    const nome =
        document.getElementById("nome").value.trim();

    const email =
        document.getElementById("email").value.trim();

    const senha =
        document.getElementById("senha").value;


    mostrarMensagem("Cadastrando usuário...", false);


    try {

        const response = await fetch("/usuarios", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                nome,
                email,
                senha
            })

        });


        if (response.status === 409) {

            const erro = await response.json();

            mostrarMensagem(
                erro.erro || "E-mail já cadastrado.",
                true
            );

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Não foi possível cadastrar o usuário."
            );

        }


        document
            .getElementById("usuario-form")
            .reset();


        document
            .getElementById("usuario-form-container")
            .classList.add("hidden");


        limparMensagem();


        await carregarUsuarios();


    } catch (error) {

        console.error(error);

        mostrarMensagem(
            "Erro ao cadastrar usuário.",
            true
        );

    }
}


function mostrarMensagem(mensagem, erro) {

    const elemento =
        document.getElementById("usuario-form-mensagem");

    elemento.textContent = mensagem;

    elemento.className =
        erro ? "mensagem erro" : "mensagem sucesso";
}


function limparMensagem() {

    const elemento =
        document.getElementById("usuario-form-mensagem");

    elemento.textContent = "";

    elemento.className = "";
}