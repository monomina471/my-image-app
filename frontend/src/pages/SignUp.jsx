import { useState } from "react";
import Header from "../components/Header";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";
import '../authpage.css'

function SignUp() {

    const [name, setName] = useState("")
    const [password, setPassword] = useState("")
    const [email, setEmail] = useState("")

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

    const navigate = useNavigate();

    const handleClick = async(e) => {

        e.preventDefault();

        try{
            const res = await api.post(`${API_BASE_URL}/api/users/signup`, { email, password, name },
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )

            const data = res.data;

            navigate("/LoginPage", { state: { flashMessage: "ユーザー登録が完了しました! ログインしてください。"}});

        } catch (error) {
            console.error("Signup failed:", error)

            if(error.response.status === 409 || error.response.status === 500){
                alert("このメールアドレスは既に使用されています");
            } else {
                alert("サーバーエラーが発生しました");
            }
        }
    }

    return (
        <>
            <Header />
                        <div className="auth-page">
            <div className="auth-card">
            <h2>ユーザー登録</h2>
            <form onSubmit={handleClick} >
                <div className="form-group">
                <label htmlFor="name" id="name">ユーザー名</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={name}
                    minLength="4"
                    maxLength="10"
                    required
                    placeholder="ユーザー名"
                    onChange={(e) => setName(e.target.value)}
                />
                </div>

                <div className="form-group">
                <label htmlFor="password" id="password">パスワード</label>
                <input type="password"
                    id="password"
                    name="password"
                    value={password}
                    minLength="8"
                    required
                    placeholder="パスワード(8文字以上)"
                    onChange={(e) => setPassword(e.target.value)}
                />
                </div>
                <div className="form-group">
                <label htmlFor="email" id="email">メールアドレス</label>
                <input type="email"
                    id="email"
                    name="email"
                    value={email}
                    required
                    placeholder="メールアドレス"
                    onChange={(e) => setEmail(e.target.value)}
                />
                </div>
                <button type="submit" className="auth-button">
                    登録
                </button>
            </form>
                                <div className="auth-link">
                        <a href="/LoginPage">アカウント登録済の場合はこちら</a>
                    </div>
            </div>
            </div>



        </>
    )
}

export default SignUp;