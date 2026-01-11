import { useEffect, useState } from "react";
import { getTasks } from "../api/TaskApi";
import { useAuth } from "../auth/AuthContext";
import type {Task} from "../models/Task";
import {createTask as createTaskAPI, deleteTask as deleteTaskAPI} from "../api/TaskApi.ts";

export default function TasksPage() {
    const { token } = useAuth();
    const [tasks, setTasks] = useState<Task[]>([]);

    const [taskName, setTaskName] = useState("");
    const [taskDesc, setTaskDesc] = useState("")

    const [errorMessage, setErrorMessage] = useState<string>("");

    useEffect(() => {
        if (!token) return;
        getTasks(token).then(setTasks);
    }, [token]);

    async function createTask() {
        if(taskName.trim().length == 0 || taskDesc.trim().length == 0)
        {
            setErrorMessage("Name and description cannot be empty");
            return;
        }
        setErrorMessage("");
        if (!token) return;
        await createTaskAPI(taskName, taskDesc, token);
        getTasks(token).then(setTasks);
    }

    async function deleteTask(id:string) {
        await deleteTaskAPI(id, token || "");
        setTasks(tasks => tasks.filter(t => t.id !== id));
    }

    return (
        <div>
            <p>{errorMessage}</p>
            <input placeholder="Task name" onChange={e => setTaskName(e.target.value)}/>
            <input placeholder="Task desc" onChange={e => setTaskDesc(e.target.value)}/>
            <button onClick={createTask}>Create task</button>
            <ul>
                {tasks.map(t => (
                    <li key={t.id}>{t.name} : {t.description} <button onClick={()=>deleteTask(t.id)}>Delete task</button></li>
                ))}
            </ul>
        </div>
    );
}