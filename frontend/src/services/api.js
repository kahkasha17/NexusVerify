
const API_BASE_URL = 'http://localhost:8080/api';

async function apiRequest(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || 'Something went wrong');
        }

        return data;

    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

export const api = {

    verify: (type, input) =>
        apiRequest('/verify', {
            method: 'POST',
            body: JSON.stringify({ type, input }),
        }),

    getHistory: () =>
        apiRequest('/history', {
            method: 'GET',
        }),

    clearHistory: () =>
        apiRequest('/history', {
            method: 'DELETE',
        }),
};

export default api;
