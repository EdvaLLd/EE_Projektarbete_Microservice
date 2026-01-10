import {useEffect, useState} from "react";
import type {User} from "../models/User.ts";
import {getAllUsers, deleteUser as deleteUserAPI} from "../api/AuthApi";
import {useAuth} from "../auth/AuthContext.tsx";

export default function RegisterPage() {

    const [users, setUsers] = useState<User[]>([]);
    const { token } = useAuth();
    useEffect(() => {
        getAllUsers().then(setUsers);
    }, []);

    async function deleteUser(username:string) {
        await deleteUserAPI(username, token || "");
        setUsers(prevUsers => prevUsers.filter(u => u.username !== username));
    }

    return (
        <div>
            <ul>
                {users.map(u => (!u.roles.includes("ROLE_ADMIN")?
                        (
                            <li key={u.username}>{u.username} <button onClick={()=>deleteUser(u.username)}>Remove User</button>
                            </li>
                        ):null
                ))}
            </ul>
        </div>
    );
}