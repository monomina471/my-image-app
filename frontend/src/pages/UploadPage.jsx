import { useState } from "react";
import DragDropZone from "../components/DragDropZone";
import Header from "../components/Header";
import api from "../api/axios";

function UpLoadPage() {

    const [files, setFiles] = useState([]);
    const [tags, setTags] = useState(["", "", "", "", ""]);

    const token = localStorage.getItem("JWT")

    //const FormData = required("form-data");

    // 配列のコピーを作成、指定した場所の値を更新、Stateを更新
    const handleTagChange = (index, value) => {
        const newTags = [...tags];
        newTags[index] = value;
        setTags(newTags);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (files.length == 0) return;

        const formData = new FormData();

        formData.append("file", files[0]); //formDataにファイルを追加

        const tagsString = tags
            .map(tag => tag.trim()) // 前後の空白を削除
            .filter(tag => tag !== "") // 空文字を除外
            .join(","); // "タグ1,タグ2,タグ3" の形にする

        formData.append("tags", tagsString); //formDataにタグを追加

        const userId = localStorage.getItem("userId");
        if (userId) {
            formData.append("userId", userId); //formDataに作成者IDを追加
        }

        try {
            await api.create().post("http://localhost:8080/api/images/save", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    Authorization: `Bearer ${token}`
                }
            });

            alert("登録完了!");
            setFiles([]); //ファイルとタグをリセット
            setTags(["", "", "", "", ""]);

        } catch (error) {
            console.error(error);
            alert("エラーが発生しました");
        }

    };

    return (
        <>
            <Header />
            <form onSubmit={(e) => {
                handleSubmit(e);
            }}>
                <DragDropZone onFilesSelected={(selected) => setFiles(selected)}
                files={files} />
                <br />
                <div style={{ marginTop: "20px" }}>
                    <p>タグ入力 (最大5つ)</p>
                    {/* map関数を使って5つのinputを自動生成 */}
                    {tags.map((tag, index) => (
                        <div key={index} style={{ marginBottom: "8px" }}>
                            <label htmlFor={`tag-${index}`} style={{ marginRight: "10px" }}>
                                タグ {index + 1}:
                            </label>
                            <input
                                type="text"
                                id={`tag-${index}`}
                                value={tag}
                                placeholder={`タグ ${index + 1} を入力`}
                                onChange={(e) => handleTagChange(index, e.target.value)}
                            />
                        </div>
                    ))}
                </div>
                <br />
                <br />
                <button type="submit">登録</button>
            </form>

        </>
    )
}

export default UpLoadPage;