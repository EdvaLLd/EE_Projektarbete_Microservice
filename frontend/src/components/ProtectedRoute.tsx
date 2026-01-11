import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

interface ProtectedRouteProps {
    children: JSX.Element;
}

export default function ProtectedRoute({ children }: ProtectedRouteProps) {
    const { user } = useAuth();

    if (!user) {
        // Om ingen användare är inloggad, skicka till login
        return <Navigate to="/login" replace />;
    }

    // Om användaren är inloggad, rendera barn-komponenten
    return children;
}