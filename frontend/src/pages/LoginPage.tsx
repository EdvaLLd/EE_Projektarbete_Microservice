import { useState } from "react";
import { login as loginApi } from "../api/AuthApi";
import { useAuth } from "../auth/AuthContext";

export default function LoginPage() {
    const { login } = useAuth();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    async function handleLogin() {
        const { user, token } = await loginApi(username, password);
        login(user, token);
    }

    return (
        <div>
            <input placeholder="Username" onChange={e => setUsername(e.target.value)} />
            <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} />
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}