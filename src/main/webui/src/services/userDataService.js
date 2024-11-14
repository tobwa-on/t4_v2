import apiService from '@/services/apiService';

export const changePassword = async (uid, currentPassword, newPassword) => {
    const params = new URLSearchParams();
    params.append('uid', uid);
    params.append('currentPassword', currentPassword);
    params.append('newPassword', newPassword);

    try {
        const response = await apiService.post('/user/change-password', params, {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });
        return { success: true, message: 'Passwort erfolgreich geändert.' };
    } catch (error) {
        let errorMessage = 'Fehler beim Ändern des Passworts.';
        if (error.response && error.response.data) {
            errorMessage = error.response.data;
        }
        return { success: false, message: errorMessage };
    }
};

export const registerNewUser = async (username, password) => {
    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);

    try {
        const response = await apiService.post('/user/register', params, {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });
        return { success: true, message: response.data.message };
    } catch (error) {
        return { success: false, message: error.response?.data || "Registrierung fehlgeschlagen" };
    }
};

export const fetchAllUsers = async () => {
    try {
        const response = await apiService.get('/user/all');
        return response.data;
    } catch (error) {
        console.error("Fehler beim Abrufen der Benutzerliste:", error);
        throw error;
    }
};

export const deleteUser = async (uid) => {
    try {
        const response = await apiService.delete(`/user/delete=${uid}`);
        return response.data;
    } catch (error) {
        console.error("Fehler beim Löschen des Benutzers:", error);
        throw error;
    }
};
