import { useState } from "react";
import Header from "../components/Header";
import { useLocation, useNavigate } from "react-router-dom";
import api from '../api/axios';


function LoginPage() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")

    const navigate = useNavigate();
    const location = useLocation();

    const message = location.state?.flashMessage;

    const handleClick = async (e) => {

        e.preventDefault();

        try {
            const res = await api.post("http://localhost:8080/api/users/login", { email, password },
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )

            const { userId, accessToken, refreshToken } = res.data

            localStorage.setItem("userId", userId) //ログイン後に使うためにIDを保存

            localStorage.setItem("JWT", accessToken) //トークンを保存

            localStorage.setItem("refreshToken", refreshToken)

            navigate("/ImageListPage") //画像一覧にリダイレクト

        } catch (error) {
            console.error("Login failed:", error);

            if (error.response && error.response.status == 401) {
                setError("メールアドレスかパスワードが間違っています");
            } else {
                setError("サーバーエラーが発生しました")
            }
        }
    };

    return (
        <>
            <Header />

            {/* サインアップから飛んだ場合のみ、緑色の帯などで表示する */}
            {message && ( /* 変数messageが存在する場合のみ右側を評価する */
                <div style={{
                    backgroundColor: '#d4edda',
                    color: '#155724',
                    padding: '10px',
                    marginBottom: '15px',
                    borderRadius: '5px',
                    border: '1px solid #c3e6cb'
                }}>
                    {message}
                </div>
            )}

            <p>ログインページ</p>
            <form onSubmit={handleClick}>
                <label htmlFor="email" id="email">メールアドレス:</label>
                <input
                    type="text"
                    id="email"
                    name="email"
                    value={email}
                    placeholder="メールアドレス"
                    required
                    onChange={(e) => setEmail(e.target.value)}
                />
                <br />
                <label htmlFor="password" id="password">パスワード:</label>
                <input type="password"
                    id="password"
                    name="password"
                    value={password}
                    minLength="8"
                    placeholder="パスワード(8文字以上)"
                    required
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br />
                <br />
                <button type="submit">ログイン</button>
                <br />
                <br />
                <a href="http://localhost:5173/SignUp">アカウント登録がまだの場合はこちらから</a>

            </form>
        </>
    )
}

export default LoginPage;
