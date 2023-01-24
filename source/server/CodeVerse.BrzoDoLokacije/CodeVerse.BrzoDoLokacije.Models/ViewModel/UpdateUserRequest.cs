using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class UpdateUserRequest
    {
        [Required]
        public long Id { get; set; }
        [Required]
        public string Username { get; set; }
        [Required]
        public string FirstName { get; set; }
        [Required]
        public string LastName { get; set; }
        public bool ChangeProfilePicture { get; set; } = false;
        public string? ProfilePictureBase64 { get; set; } = string.Empty;
    }
}
