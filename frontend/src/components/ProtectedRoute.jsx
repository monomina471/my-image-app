import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children }) {
    const jwt = localStorage.getItem("JWT");
    const userId = localStorage.getItem("userId");

    if (!jwt || !userId) {
        return <Navigate to="/LoginPage" replace state={{ flashMessage: "ログインをしてからアクセスしてください"}}/>
    }

    return children;
}