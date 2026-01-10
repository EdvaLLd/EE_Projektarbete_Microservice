import type {Task} from "../models/Task";

const TASK_URL = "http://localhost:8082/task";

export async function getTasks(token: string): Promise<Task[]> {
    const res = await fetch(`${TASK_URL}/getall`, {
        headers: {
            Authorization: token
        }
    });

    if (!res.ok) {
        throw new Error("Unauthorized");
    }

    return res.json();
}


export async function createTask(
    name: string,
    description: string,
    token: string
): Promise<void> {

    const res = await fetch(`${TASK_URL}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: token
        },
        body: JSON.stringify({ name: name, description: description})
    });

    if (!res.ok) {
        throw new Error("Creating task failed");
    }
}

export async function deleteTask(
    id: string,
    token: string
): Promise<void> {

    const res = await fetch(`${TASK_URL}/${encodeURIComponent(id)}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            Authorization: token
        },
    });

    if (!res.ok) {
        throw new Error("Deleting task failed");
    }
}