async function carregarUsuarios() {

    const lista = document.getElementById("usuarios-lista");

    try {

        const response = await fetch("/usuarios");

        if (!response.ok) {
            throw new Error("Erro ao buscar usuários.");
        }

        const usuarios = await response.json();

        lista.innerHTML = "";

        usuarios.forEach(usuario => {

            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${usuario.id}</td>
                <td>${usuario.nome}</td>
                <td>${usuario.email}</td>
                <td>${usuario.perfil}</td>
                <td>${usuario.ativo ? "Ativo" : "Inativo"}</td>
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