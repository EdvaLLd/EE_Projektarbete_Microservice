import { Link } from "react-router-dom";
import {useAuth} from "../auth/AuthContext.tsx";

export default function Navbar() {
    const { user, logout, isAdmin} = useAuth();

    return (
        <nav style={{ padding: "1rem", borderBottom: "1px solid #ccc" }}>
            {user ? (
                <>
                    <Link to="/tasks">Tasks</Link> |{" "}
                    <Link to="/me">About user</Link> |{" "}
                    <button onClick={logout}>Logout</button>
                    {
                        isAdmin?(<>
                        <Link to="/deleteUser">Remove user</Link> |{" "}
                        </>):null
                    }
                </>
            ) : (
                <>
                    <Link to="/login">Login</Link>
                    <Link to="/register">Register</Link>
                </>
            )}

        </nav>
    );
}