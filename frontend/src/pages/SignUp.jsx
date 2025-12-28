import { useState } from "react";
import Header from "../components/Header";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

function SignUp() {

    const [name, setName] = useState("")
    const [password, setPassword] = useState("")
    const [email, setEmail] = useState("")

    const navigate = useNavigate();

    const handleClick = async(e) => {

        e.preventDefault();

        try{
            const res = await axios.post("http://localhost:8080/api/users/signup", { email, password, name },
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
                console.log("このメールアドレスは既に使用されています");
            } else {
                console.log("サーバーエラーが発生しました");
            }
        }
    }

    return (
        <>
            <Header />
            <p>ユーザー登録</p>
            <form onSubmit={handleClick}>
                <label htmlFor="name" id="name">Name:</label>
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
                <br />
                <label htmlFor="password" id="password">Password:</label>
                <input type="password"
                    id="password"
                    name="password"
                    value={password}
                    minLength="8"
                    required
                    placeholder="パスワード(8文字以上)"
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br />
                <label htmlFor="email" id="email">Email:</label>
                <input type="email"
                    id="email"
                    name="email"
                    value={email}
                    required
                    placeholder="メールアドレス"
                    onChange={(e) => setEmail(e.target.value)}
                />
                <br />
                <button type="submit">登録</button>
            </form>



        </>
    )
}

export default SignUp;