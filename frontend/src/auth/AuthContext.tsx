import { createContext, useContext, useState } from "react";
import type {User} from "../models/User";

interface AuthContextType {
    user: User | null;
    token: string | null;
    login: (user: User, token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>(null!);

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);

    function login(user: User, token: string) {
        setUser(user);
        setToken(token);
    }

    function logout() {
        setUser(null);
        setToken(null);
    }

    return (
        <AuthContext.Provider value={{ user, token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used inside AuthProvider");
    return {
        ...context,
        isAdmin: context.user?.roles.includes("ROLE_ADMIN") ?? false
    };
};