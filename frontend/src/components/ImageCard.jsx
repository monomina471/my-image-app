import '../ImageCard.css'
import DeleteButton from "./DeleteButton";

function ImageCard({ image, onDeleteSuccess, handleClick, getImageUrl }) {

    const { url, tags, createTime, id } = image;

    //文字列ならそのままで、配列ならコンマで区切る
    const displayTags = Array.isArray(tags) ? tags.join(',') : tags;

    return (
        <div className="card">
            <img className="img"
                src={getImageUrl(url)}
                alt={tags}
                onClick={handleClick}
                style={{ cursor: "pointer" }} />

            <div className="tags">
                タグ:<br /> {displayTags || "タグなし"}
            </div>

            <div>
                {createTime ? new Date(createTime).toLocaleString() : "作成日時不明"}
            </div>

            <DeleteButton id={id} onDeleteSuccess={onDeleteSuccess} />

        </div>
    )
}

export default ImageCard;