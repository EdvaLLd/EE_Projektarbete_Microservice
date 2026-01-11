import type {User} from "../models/User";
import type {Task} from "../models/Task.ts";
import {deleteAllTasksFromUser} from "./TaskApi.ts";

const AUTH_URL = "http://localhost:8081/auth";

export async function login(
    username: string,
    password: string
): Promise<{ user: User; token: string }> {

    const res = await fetch(`${AUTH_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    if (!res.ok) {
        const error = await res.text();
        throw new Error("login failed. " + res.status + ": " + error);
    }

    const data = await res.json();
    const { user, token } = data;

    return { user, token };
}

export async function getAllUsers():
    Promise<User[]> {

    const res = await fetch(`${AUTH_URL}/getAllUsers`, {
        method: "GET",

    });

    if (!res.ok) {
        const error = await res.text();
        throw new Error("get all users failed. " + res.status + ": " + error);
    }

    return await res.json();
}

export async function register(
    username: string,
    password: string,
    adminPass: string
): Promise<void> {

    const res = await fetch(`${AUTH_URL}/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password, adminPass })
    });

    if (!res.ok) {
        const error = await res.text();
        throw new Error("register user failed. " + res.status + ": " + error);
    }
}

export async function deleteUser(username: string, token: string): Promise<void> {
    await deleteAllTasksFromUser(username, token);
    const res = await fetch(`${AUTH_URL}/remove?username=${encodeURIComponent(username)}`, {
        method: "DELETE",
        headers: {
            Authorization: token
        }
    });

    if (!res.ok) {
        const error = await res.text();
        throw new Error("delete user failed. " + res.status + ": " + error);
    }
}

export async function me(token: string): Promise<Task[]> {
    const res = await fetch(`${AUTH_URL}/me`, {
        headers: {
            Authorization: token
        }
    });

    if (!res.ok) {
        const error = await res.text();
        throw new Error("get user info failed. " + res.status + ": " + error);
    }

    return res.json();
}