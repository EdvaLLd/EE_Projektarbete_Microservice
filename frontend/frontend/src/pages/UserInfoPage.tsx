import {useAuth} from "../auth/AuthContext.tsx";

export default function LoginPage() {
    const { user } = useAuth();

    return (
            user ?
                <div>
                    <h1>{user.username}</h1>
                    <ul>
                        {user.roles.map(r => (
                            <li key={r}>{r}</li>
                        ))}
                    </ul>
                </div>
                :
                <div>
                    Not logged in
                </div>
    );
}