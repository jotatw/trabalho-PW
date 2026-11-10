async function apiFetch(url, options = {}) {

    const token = sessionStorage.getItem("token");

    const headers = new Headers(options.headers || {});

    if (token) {
        headers.set("Authorization", `Bearer ${token}`);
    }

    const response = await fetch(url, {
        ...options,
        headers
    });

    // Token ausente, inválido ou expirado.
    if (response.status === 401) {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("usuario");

        window.location.replace("/");
    }

    return response;
}
