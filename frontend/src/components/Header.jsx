import { Link, useNavigate } from "react-router-dom";
import '../Header.css';
import { useState } from "react";

function Header() {

    //const [menuOpen, setMenuOpen] = useState(false);

    const navigate = useNavigate();

    //const toggleFunction = () => {setMenuOpen((prevState) => !prevState)}

    const logout = () => {
        localStorage.removeItem("userId");
        navigate("/LoginPage")
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