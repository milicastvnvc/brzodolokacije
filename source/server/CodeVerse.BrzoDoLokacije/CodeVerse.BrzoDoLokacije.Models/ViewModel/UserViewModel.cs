using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class UserViewModel
    {
        public long Id { get; set; }
        public string Username { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public string FirstName { get; set; } = string.Empty;
        public string LastName { get; set; } = string.Empty;
        public double AvgRating { get; set; }
        public double NumberOfRatings { get; set; }
        public string? ProfilePicturePath { get; set; }
        public bool IsLikedByCurrentUser { get; set; }
        public bool? IsVerified { get; set; }
    }
}