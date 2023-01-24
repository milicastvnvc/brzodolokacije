using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class UserProfileResponse
    {
        public double AvgRating { get; set; }
        public int NumberOfRatings { get; set; }
        public int NumberOfPosts { get; set; }
        public List<PostViewModel> Posts { get; set; }
    }
}
