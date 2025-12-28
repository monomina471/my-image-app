import { Route, Routes } from "react-router-dom";
import ImageListPage from "../pages/ImageListPage.jsx"
import LoginPage from "../pages/LoginPage.jsx"
import SignUp from "../pages/SignUp.jsx"
import UploadPage from "../pages/UploadPage.jsx"
import ProtectedRoute from "../components/ProtectedRoute.jsx";

function AppRoutes() {
    return (
        <Routes>
            {/* ログイン不要 */}
            <Route path="/LoginPage" element={<LoginPage />} />
            <Route path="/SignUp" element={<SignUp />} />

            <Route path="/ImageListPage" element={
                <ProtectedRoute>
                    <ImageListPage />
                </ProtectedRoute>} />
            <Route path="/UploadPage" element={
                <ProtectedRoute>
                    <UploadPage />
                </ProtectedRoute>} />

        </Routes>
    )

}

export default AppRoutes;
