import { useState } from "react";
import Header from "../components/Header";
import { useLocation, useNavigate } from "react-router-dom";
import api from '../api/axios';
import '../authpage.css'


function LoginPage() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    const [loading, setLoading] = useState(false);

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

    const navigate = useNavigate();
    const location = useLocation();
    const message = location.state?.flashMessage; // オプショナルチェーン演算子

    const handleClick = async (e) => {

        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            const res = await api.post(
                `${API_BASE_URL}/api/users/login`,
                { email, password },
                { headers: { 'Content-Type': 'application/json' } }
            );

            const { userId, accessToken, refreshToken } = res.data;

            localStorage.setItem("userId", userId); // ログイン後に使うためにIDを保存
            localStorage.setItem("JWT", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            navigate("/ImageListPage"); // 画像一覧にリダイレクト

        } catch (error) {
            console.error("Login failed:", error);

            if (error.response && error.response.status == 401) {
                setError("メールアドレスかパスワードが間違っています");
            } else {
                setError("サーバーエラーが発生しました")
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <Header />
            <div className="auth-page">
                <div className="auth-card">
                    <h2>ログイン</h2>

                    {/* サインアップから飛んだ場合のみ、緑色の帯などで表示する */}
                    { /* 変数messageかerrorが存在する場合のみ右側を評価する */}
                    {message && <div className="flash-message">{message}</div>}
                    {error && <div className="error-message">{error}</div>}



                    <form onSubmit={handleClick}>
                        <div className="form-group">
                            <label htmlFor="email">メールアドレス</label>
                            <input
                                type="text"
                                id="email"
                                name="email"
                                value={email}
                                placeholder="メールアドレス"
                                required
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">パスワード</label>
                            <input type="password"
                                id="password"
                                name="password"
                                value={password}
                                minLength="8"
                                placeholder="パスワード(8文字以上)"
                                required
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>

                        <button
                            type="submit"
                            className="auth-button"
                            disabled={loading}
                        >
                            {loading ? "ログイン中..." : "ログイン"}
                        </button>
                    </form>
                    <div className="auth-link">
                        <a href="/SignUp">アカウント登録がまだの場合はこちらから</a>
                    </div>
                </div>
            </div>
        </>
    )
}

export default LoginPage;
