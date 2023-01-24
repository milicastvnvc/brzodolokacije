namespace CodeVerse.BlogsDemo.API.Models.Entity
{
    public class Blog
    {
        public Blog()
        {
            Posts = new List<Post>();
        }

        public long Id { get; set; }
        public string Title { get; set; } = string.Empty;
        public virtual List<Post> Posts { get; set; }
    }
}