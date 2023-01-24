using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace CodeVerse.BrzoDoLokacije.API.Controllers
{
    public class UserController : AuthorizeBaseController
    {
        private readonly IUserBL _userBL;

        public UserController(IUserBL userBL)
        {
            _userBL = userBL;
        }

        [HttpPost]
        public async Task<IActionResult> UpdateUser([FromBody] UpdateUserRequest user)
        {
            var result = await _userBL.UpdateUser(user);
            return Ok(result);
        }

        [HttpPost("LikeUser")]
        public async Task<IActionResult> LikeUser([FromBody] long userId)
        {
            var result = await _userBL.LikeUser(userId);
            return Ok(result);
        }

        [HttpPost("DislikeUser")]
        public async Task<IActionResult> DislikeUser([FromBody] long userId)
        {
            var result = await _userBL.DislikeUser(userId);
            return Ok(result);
        }

        [HttpGet("GetLikedUsers")]
        public async Task<IActionResult> GetLikedUsers([FromQuery] int pageIndex = 0, int pageSize = 20, string? searchTerm = null)
        {
            var result = await _userBL.GetCurrentUserLikedUsers(pageIndex, pageSize, searchTerm);
            return Ok(result);
        }

        [HttpGet("{userId:long}")]
        public async Task<IActionResult> GetUser([FromRoute] long userId)
        {
            var result = await _userBL.GetUserById(userId);
            return Ok(result);
        }
    }
}
