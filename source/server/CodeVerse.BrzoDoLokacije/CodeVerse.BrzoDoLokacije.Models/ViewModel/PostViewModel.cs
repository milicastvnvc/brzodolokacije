using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class PostViewModel
    {
        public long Id { get; set; }
        public string Description { get; set; }
        public DateTime DateCreated { get; set; }
        public double LongitudeCenter { get; set; }
        public double LatitudeCenter { get; set; }
        public string? PlaceName { get; set; }
        public string? Text { get; set; }
        public string? PlaceNameSr { get; set; }
        public UserViewModel CreatedBy { get; set; }
        public double AvgRating { get; set; } = 0;
        public int NumberOfRatings { get; set; } = 0;
        public int NumberOfComments { get; set; } = 0;
        public int? UserRate { get; set; } = null;
        public List<ResourceViewModel> Resources { get; set; }
        public List<string> Tags { get; set; }
        public List<CommentViewModel> Comments { get; set; }
    }
}
