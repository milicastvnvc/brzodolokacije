using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.Entity
{
    public class Post
    {
        public long Id { get; set; }
        public string? Description { get; set; }
        public DateTime DateCreated { get; set; }
        [ForeignKey("FK_CreatedById")]
        public long CreatedById { get; set; }
        public User CreatedBy { get; set; }
        public string LongitudeCenter { get; set; }
        public string LatitudeCenter { get; set; }
        public string? PlaceName { get; set; }
        public string? Text { get; set; }
        public string? PlaceNameSr { get; set; }
        public string? Tags { get; set; }
        public string? PostSearchTerm { get; set; }
        public virtual List<Resource> Resources { get; set; }
        public virtual List<Comment> Comments { get; set; }
        public virtual List<Rating> Ratings { get; set; }
    }
}
