import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080/api",
});

// レスポンスの割り込み処理
api.interceptors.response.use(
    (response) => {
        return response; // 成功時はそのまま返す
    },
    async (error) => {
        const originalRequest = error.config;

        // エラーが403(権限なし)で、まだ再試行していない場合
        if (error.response && error.response.status === 403 && !originalRequest._retry) {
            originalRequest._retry = true; // 無限ループ防止フラグ

            try {
                // リフレッシュトークンを使って新しいJWTを要求
                const refreshToken = localStorage.getItem("refreshToken");
                const response = await axios.post("http://localhost:8080/api/users/refresh", {
                    token: refreshToken
                });

                // 新しいトークンを保存
                const { token } = response.data;
                localStorage.setItem("JWT", token);

                // 失敗した元のリクエストのヘッダーを書き換えて再送
                originalRequest.headers["Authorization"] = `Bearer ${token}`;
                return api(originalRequest);

            } catch (refreshError) {
                // リフレッシュもダメなら完全にログアウトさせる
                console.error("セッション切れ", refreshError);
                localStorage.clear();
                window.location.href = "/LoginPage";
            }
        }
        return Promise.reject(error);
    }
);

export default api;