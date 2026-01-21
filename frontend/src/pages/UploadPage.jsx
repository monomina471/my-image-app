import { useEffect, useState } from "react";
import DragDropZone from "../components/DragDropZone";
import Header from "../components/Header";
import api from "../api/axios";
import '../UploadPage.css'

function UpLoadPage() {

    const [files, setFiles] = useState([]);
    const [tags, setTags] = useState(["", "", "", "", ""]);
    const [sampleTags, setSampleTags] = useState([]);

    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

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
            await api.create().post(`${API_BASE_URL}/api/images/save`, formData, {
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

    useEffect(() => {
        const fetchSampleTags = async () => {
            const userId = localStorage.getItem("userId");

            const res = await api.get(`${API_BASE_URL}/api/images/tags/${userId}/sample`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            setSampleTags(res.data); // タグのリストを格納
        };
        fetchSampleTags();
    }, []);

    const addTagFromSample = (tag) => {
        const index = tags.findIndex(t => t === "");
        if (index === -1) return; // 空きなし

        const newTags = [...tags];
        newTags[index] = tag;
        setTags(newTags);
    };

    return (
        <>
            <Header />

            <div className="upload-page">
                <div className="upload-card">
                    <h2 className="upload-title">画像をアップロード</h2>



                    <form onSubmit={handleSubmit}>
                        <div className="dropzone-wrapper">
                            <DragDropZone
                                onFilesSelected={(selected) => setFiles(selected)}
                                files={files}
                            />
                        </div>

                        <div className="tag-section">
                            <p className="tag-title">タグ入力（最大5つ）</p>

                            {tags.map((tag, index) => (
                                <input
                                    key={index}
                                    type="text"
                                    className="tag-input"
                                    placeholder={`タグ ${index + 1}`}
                                    value={tag}
                                    onChange={(e) => handleTagChange(index, e.target.value)}
                                />
                            ))}
                        </div>

                        <button type="submit">登録</button>

                        <div className="tag-sample">
                            <p>あなたが使っているタグは......</p>
                            <div className="tag-chip-list">
                                {sampleTags.map(tag => (
                                    <span
                                        key={tag}
                                        className="tag-chip"
                                        onClick={() => addTagFromSample(tag)}
                                    >
                                        #{tag}
                                    </span>
                                ))}
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </>
    )
}

export default UpLoadPage;