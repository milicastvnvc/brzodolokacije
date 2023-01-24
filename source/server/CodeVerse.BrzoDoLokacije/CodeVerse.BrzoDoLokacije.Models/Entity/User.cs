using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.Entity
{
    public class User
    {
        public long Id { get; set; }
        public string Username { get;set; } = string.Empty;
        public string Password { get;set; } = string.Empty;
        public string Email { get;set; } = string.Empty;
        public string FirstName { get; set; } = string.Empty;
        public string LastName { get; set; } = string.Empty;
        public string? Token { get; set; } = string.Empty;
        public DateTime? TokenExpiration { get; set; }
        public bool? IsVerified { get; set; }
        public string? RefreshToken { get; set; }
        public DateTime? RefreshTokenCreated { get; set; }
        public DateTime? RefreshTokenExpires { get; set; }
        public string? ProfilePicturePath { get; set; }
        public virtual List<Post> Posts { get; set; }
        public virtual List<Comment> Comments { get; set; }
        public virtual List<Rating> Ratings { get; set; }
        [InverseProperty("LikedBy")]
        public virtual List<LikedUser> Users { get; set; }
        [InverseProperty("User")]
        public virtual List<LikedUser> LikedUsers { get; set; }
    }
}
