import { Link, useNavigate } from "react-router-dom";
import '../Header.css';

function Header() {

    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("userId");
        localStorage.removeItem("JWT");
        localStorage.removeItem("refreshToken");
        navigate("/LoginPage", { state: { flashMessage: "ログアウトしました。"}})
    }

    return (
        <header className="header">
            <nav>
                <ul className="nav-links">
                    <li>
                        <Link to="/ImageListPage">画像一覧</Link>
                    </li>
                    <li>
                        <Link to="/LoginPage">ログインページ</Link>
                    </li>
                    <li>
                        <Link to="/SignUp">アカウント登録ページ</Link>
                    </li>
                    <li>
                        <Link to="/UploadPage">画像アップロード</Link>
                    </li>
                    <li>
                        <button onClick={logout}>ログアウト</button>
                    </li>
                </ul>
            </nav>

        </header>
    )
}




export default Header;