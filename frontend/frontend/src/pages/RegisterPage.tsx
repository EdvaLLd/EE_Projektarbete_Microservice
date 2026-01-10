
import {useState} from "react";
import {register} from "../api/AuthApi.ts";
import {useNavigate} from "react-router-dom";

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [adminPassword, setAdminPassword] = useState("");
    const navigate = useNavigate();

    async function handleLogin() {
        await register(username, password, adminPassword);
        navigate("/login");
    }

    return (
        <div>
            <input placeholder="Username" onChange={e => setUsername(e.target.value)}/>
            <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)}/>
            <input type="password" placeholder="AdminPassword" onChange={e => setAdminPassword(e.target.value)}/>
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}