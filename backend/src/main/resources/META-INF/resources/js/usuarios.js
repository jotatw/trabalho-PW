let usuarioEmEdicao = null;

async function iniciarGestaoUsuarios() {
    const formulario = document.getElementById('usuario-form');
    document.getElementById('novo-usuario-button').addEventListener('click', () => abrirFormularioUsuario());
    document.getElementById('cancelar-usuario-button').addEventListener('click', fecharFormularioUsuario);
    formulario.addEventListener('submit', salvarUsuario);
    await carregarPerfis();
    await carregarUsuarios();
}

function abrirFormularioUsuario(usuario = null) {
    usuarioEmEdicao = usuario?.id ?? null;
    const formulario = document.getElementById('usuario-form');
    formulario.reset();
    document.getElementById('nome').value = usuario?.nome ?? '';
    document.getElementById('email').value = usuario?.email ?? '';
    document.getElementById('perfilId').value = usuario?.perfilId ?? '';
    document.getElementById('senha').required = !usuario;
    document.getElementById('senha').placeholder = usuario ? 'Deixe vazio para manter a senha atual' : '';
    document.querySelector('#usuario-form .form-header h3').textContent = usuario ? 'Editar usuário' : 'Novo usuário';
    document.querySelector('#usuario-form button[type="submit"]').textContent = usuario ? 'Salvar alterações' : 'Cadastrar usuário';
    limparMensagem();
    document.getElementById('usuario-form-container').classList.remove('hidden');
}

function fecharFormularioUsuario() {
    document.getElementById('usuario-form-container').classList.add('hidden');
    document.getElementById('usuario-form').reset();
    usuarioEmEdicao = null;
    limparMensagem();
}

async function carregarUsuarios() {
    const lista = document.getElementById('usuarios-lista');
    lista.replaceChildren();
    try {
        const resposta = await apiFetch('/usuarios');
        if (!resposta.ok) throw new Error(await mensagemApi(resposta));
        const usuarios = await resposta.json();
        if (!usuarios.length) {
            lista.innerHTML = '<tr><td colspan="6">Nenhum usuário cadastrado.</td></tr>';
            return;
        }
        const usuarioAtual = JSON.parse(sessionStorage.getItem('usuario') || '{}');
        usuarios.forEach(usuario => {
            const linha = document.createElement('tr');
            [usuario.id, usuario.nome, usuario.email, usuario.perfil || 'Sem perfil', usuario.ativo ? 'Ativo' : 'Inativo']
                .forEach(valor => { const celula = document.createElement('td'); celula.textContent = valor; linha.append(celula); });
            const acoes = document.createElement('td');
            if (usuario.id !== usuarioAtual.id && usuario.ativo) {
                const editar = document.createElement('button');
                editar.type = 'button'; editar.textContent = 'Editar';
                editar.addEventListener('click', () => abrirFormularioUsuario(usuario));
                acoes.append(editar, document.createTextNode(' '));
                const desativar = document.createElement('button');
                desativar.type = 'button'; desativar.textContent = 'Desativar';
                desativar.addEventListener('click', () => alterarAtivo(usuario.id, false));
                acoes.append(desativar);
            } else if (usuario.id !== usuarioAtual.id) {
                const ativar = document.createElement('button');
                ativar.type = 'button'; ativar.textContent = 'Reativar';
                ativar.addEventListener('click', () => alterarAtivo(usuario.id, true));
                acoes.append(ativar);
            } else {
                acoes.textContent = 'Conta atual';
            }
            linha.append(acoes); lista.append(linha);
        });
    } catch (erro) {
        lista.innerHTML = `<tr><td colspan="6">${erro.message}</td></tr>`;
    }
}

async function salvarUsuario(evento) {
    evento.preventDefault();
    const mensagem = document.getElementById('usuario-form-mensagem');
    mensagem.textContent = 'Salvando...';
    const valor = id => document.getElementById(id).value;
    const dados = { nome: valor('nome').trim(), email: valor('email').trim(), perfilId: Number(valor('perfilId')) };
    if (valor('senha')) dados.senha = valor('senha');
    try {
        const resposta = await apiFetch(usuarioEmEdicao ? `/usuarios/${usuarioEmEdicao}` : '/usuarios', {
            method: usuarioEmEdicao ? 'PUT' : 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dados)
        });
        if (!resposta.ok) throw new Error(await mensagemApi(resposta));
        fecharFormularioUsuario(); await carregarUsuarios();
    } catch (erro) { mostrarMensagem(erro.message, true); }
}

async function alterarAtivo(id, ativo) {
    if (!window.confirm(ativo ? 'Reativar este usuário?' : 'Desativar este usuário?')) return;
    const resposta = await apiFetch(`/usuarios/${id}/status`, {
        method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ ativo })
    });
    if (!resposta.ok) window.alert(await mensagemApi(resposta));
    await carregarUsuarios();
}

async function carregarPerfis() {
    const select = document.getElementById('perfilId');
    try {
        const resposta = await apiFetch('/perfis');
        if (!resposta.ok) throw new Error(await mensagemApi(resposta));
        const perfis = await resposta.json();
        select.replaceChildren(new Option('Selecione um perfil', ''));
        perfis.forEach(perfil => select.add(new Option(perfil.nome, perfil.id)));
    } catch (erro) {
        select.replaceChildren(new Option('Não foi possível carregar perfis', ''));
        mostrarMensagem(erro.message, true);
    }
}

async function mensagemApi(resposta) {
    try { const erro = await resposta.json(); return erro.detalhes?.join(' ') || erro.erro || 'Não foi possível concluir a operação.'; }
    catch { return 'Não foi possível concluir a operação.'; }
}
function mostrarMensagem(texto, erro) { const el = document.getElementById('usuario-form-mensagem'); el.textContent = texto; el.className = erro ? 'mensagem erro' : 'mensagem sucesso'; }
function limparMensagem() { const el = document.getElementById('usuario-form-mensagem'); el.textContent = ''; el.className = ''; }
