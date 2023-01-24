namespace CodeVerse.BlogsDemo.API.Models.Entity
{
    public class Post
    {
        public Post()
        {
            Blog = new Blog();
        }

        public long Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public string Text { get; set; } = string.Empty;
        public string Author { get; set; } = string.Empty;
        public long BlogId { get; set; }
        public virtual Blog Blog { get; set; }
    }
}