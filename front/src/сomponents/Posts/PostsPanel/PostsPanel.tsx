import { useEffect, useState } from "react";
import axios from "axios";
import {
  IPostsDto,
  initIPostsDto,
} from "../../AdminPage/PostsAdmin/interfaces/IPostsDto";
import { Link } from "react-router-dom";
import styles from "./PostsPanel.module.css";
import { BsCalendar2Week } from "react-icons/bs";
import moment from "moment";

export default function PostsPanel() {
  const [posts, setPosts] = useState<IPostsDto>(initIPostsDto);
  const itemsOnPage = 4;

  useEffect(() => {
    async function getListOfPosts() {
      try {
        const response = await axios.get(
          `/api/posts?page=0&items=${itemsOnPage}&orderBy=creationTimePost&desk=true`
        );
        setPosts(response.data);
      } catch (error) {
        console.error("Error during request execution:", error);
      }
    }
    getListOfPosts();
  }, []);

  return (
    <>
      {posts && (
        <div className="container">
          {posts.posts.map(
            ({
              idPost,
              creationTimePost,
              titlePost,
              shortPostDescription,
            }) => (
              <div key={idPost}>
                <p className={styles.post_event_date}>
                  <BsCalendar2Week />{" "}
                  {moment(creationTimePost).format("D MMMM YYYY")}
                </p>
                <h4 className={styles.post_event_h5}>
                  <Link to={`/posts/${idPost}`}>{titlePost}</Link>
                </h4>
                <div className={styles.postpanel_text}>
                  <p className={styles.post_event_text}>
                    {shortPostDescription}
                  </p>
                </div>
                <hr className={styles.post_hr} />
              </div>
            )
          )}
        </div>
      )}
    </>
  );
}
