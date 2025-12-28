import api from "../api/axios";

function DeleteButton({ id, onDeleteSuccess }) {

    const token = localStorage.getItem("JWT")

    const handleClick = async (e) => {

        e.stopPropagation();

        if (!window.confirm("本当に削除しますか?")) {
            return;
        }

        try {
            await api.delete(`http://localhost:8080/api/images/delete/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            alert("削除しました");

            if (onDeleteSuccess) {
                onDeleteSuccess(id);
            }

        } catch (error) {
            console.error(error);
            alert("エラーが発生しました");
        }
    };

    return (
        <>
            <button onClick={handleClick} style={{ backgroundColor: "red", color: "white" }}>
                削除
            </button></>
    )
};

export default DeleteButton;