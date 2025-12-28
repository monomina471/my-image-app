import { useCallback } from "react";
import { useDropzone } from "react-dropzone";

const style = {
    width: 200,
    height: 150,
    border: "1px dotted #888",
    display: "flex",          // 中身を中央揃えにするために追加
    alignItems: "center",     // 上下の真ん中
    justifyContent: "center", // 左右の真ん中
    padding: "10px",          // 画像が枠にくっつきすぎないように
    boxSizing: "border-box",  // paddingを含めたサイズ計算にする
    cursor: "pointer",        // クリックできる感が出るように
    overflow: "hidden"        // 画像がはみ出さないように
};

// プレビュー画像のスタイル
const imgStyle = {
    maxWidth: "100%",  // 枠からはみ出さないように
    maxHeight: "100%", // 枠からはみ出さないように
    objectFit: "contain" // アスペクト比を維持して収める
};

function DragDropZone({ onFilesSelected, files }) {
    const onDrop = useCallback((acceptedFiles) => {
        onFilesSelected(acceptedFiles);
    }, [onFilesSelected]);

    // useDropzoneという関数を呼び出し、帰ってきたオブジェクトから必要な部分だけ取り出し
    // ファイルがドロップされたらonDropという関数を実行する
    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        onDrop,
        accept: { 'image/*': [] },
        multiple: false
    });

    return (
        // getRootProps() ドラッグ＆ドロップを実現するために <div> タグに必要なすべての属性（プロパティ）」が入ったオブジェクトを返す
        <div {...getRootProps()} style={style}> {/*その属性たちをスプレッド構文でdivタグの中に展開*/}
            <input {...getInputProps()} />
            { //ファイルがある場合はプレビューを表示、無い場合はテキストを表示
                files && files.length > 0 ? (
                    <img
                        src={URL.createObjectURL(files[0])}
                        alt="preview"
                        style={imgStyle} />
                ) : (
                    isDragActive ?
                        <p style={{textAlign: "center"}}>ファイルをドロップ ...</p> :
                        <p style={{textAlign: "center"}}>ファイルをここにドラッグするか、クリックしてファイルを選択</p>
                )
            }
        </div>
    );
}

export default DragDropZone;
