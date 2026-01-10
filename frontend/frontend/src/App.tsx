import LoginPage from "./pages/LoginPage";
import TasksPage from "./pages/TasksPage";
import UserInfoPage from "./pages/UserInfoPage.tsx";
import DeleteUserPage from "./pages/DeleteUserPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import { useAuth } from "./auth/AuthContext";
import {Route, Routes} from "react-router-dom";
import Navbar from "./components/Navbar.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";

function App() {
    const { user } = useAuth();

    return (
        <>
            <Navbar/>
            <Routes>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/tasks" element={<ProtectedRoute><TasksPage/></ProtectedRoute>}/>
                <Route path="/" element={user ? <TasksPage/> : <LoginPage/>}/>
                <Route path="/deleteUser" element={<DeleteUserPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/me" element={<ProtectedRoute><UserInfoPage/></ProtectedRoute>}/>

            </Routes>
        </>
        //user ? <TasksPage /> : <LoginPage />
    );
}

export default App;
