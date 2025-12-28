import { useEffect, useState } from "react";
import Header from "../components/Header";
import ImageCard from "../components/ImageCard";
import '../ImageCard.css'
import '../ImageClick.css'
import '../SearchFilter.css'
import api from '../api/axios';

function ImageListPage() {

    const [imageList, setImageList] = useState([]);
    const [selectedImage, setSelectedImage] = useState(null);
    const [keyword, setKeyword] = useState("");
    const [sortOrder, setSortOrder] = useState("desc"); //初期値は降順

    const userId = localStorage.getItem("userId")
    const token = localStorage.getItem("JWT")

    useEffect(() => {
        fetchImages();
    }, [sortOrder]); //sortOrderが変わるたびに検索を掛ける

    const fetchImages = async () => {
        try {
            const res = await api.get(`http://localhost:8080/api/images/user/${userId}/search`, {
                params: {
                    keyword: keyword,
                    sort: sortOrder
                },
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setImageList(res.data); //バックエンドから返された List<ImageEntity> を格納
        } catch (error) {
            console.error("API通信に失敗しました", error)
        }
    };

    const handleSearch = (e) => {
        e.preventDefault(); //フォーム送信時のリロードを防ぐ
        fetchImages();
    }

    const handleDeleteSuccess = (deletedId) => { //DeleteButtonのonDeleteSuccess(id)からidが渡される
        const newList = imageList.filter(img => img.id !== deletedId);
        setImageList(newList);
    }

    const getImageUrl = (filepath) => {
        if (!filepath) return "";
        return filepath.startsWith("http") ? path : `http://localhost:8080/uploaded/${filepath}`;
    };

    useEffect(() => {
        fetchImages()
    }, []) //ページ遷移時に起動

    return (
        <>
            <Header />

            <div className="filter-container">
                <form onSubmit={handleSearch} className="search-form">
                    <input
                        type="text"
                        className="search-input"
                        placeholder="タグで検索"
                        value={keyword}
                        onChange={(e) => setKeyword(e.target.value)}
                    />

                    <button type="submit" className="search-button">
                        検索
                    </button>
                </form>
            </div>

            <select
                className="sort-select"
                value={sortOrder}
                onChange={(e) => setSortOrder(e.target.value)}
            >
                <option value="desc">新しい順 ↓</option>
                <option value="asc">古い順 ↑</option>
            </select>

            <div style={{ padding: "20px" }}>
                <h2>画像一覧</h2>
                <div className="image-list">
                    {imageList.map((image) => (
                        <ImageCard
                            key={image.id}
                            image={image}
                            onDeleteSuccess={handleDeleteSuccess}
                            handleClick={() => setSelectedImage(image)}
                            getImageUrl={getImageUrl}
                        />))}
                </div>
            </div>

            {/*拡大表示ウインドウ*/}
            {selectedImage && (
                <div className="modal-overlay" onClick={() => selectedImage(null)}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <button className="modal-close-btn" onClick={() => setSelectedImage(null)}>
                            ✕
                        </button>

                        <img src={getImageUrl(selectedImage.url)}
                            alt="拡大表示"
                            className="modal-image" />

                        <div className="modal-info">
                            <p>タグ: {selectedImage.tags}</p>
                        </div>
                    </div>
                </div>
            )}
        </>
    )
}

export default ImageListPage;