using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Common.Constants;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Org.BouncyCastle.Utilities;
using System.Net;
using System.Net.Http.Headers;

namespace CodeVerse.BrzoDoLokacije.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AngularUserController : ControllerBase
    {
        private readonly IAngularUserBL _angularUserBL;

        public AngularUserController(IAngularUserBL angularUserBL)
        {
            _angularUserBL = angularUserBL;
        }

        [HttpPost("Register")]
        public async Task<IActionResult> AddAngularUser([FromBody] AddAngularUserRequest request)
        {
            var result = await _angularUserBL.AddAngularUser(request);
            return Ok(result);
        }

        [HttpPost("Login")]
        public async Task<IActionResult> LoginAngularUser([FromBody] AddAngularUserRequest request)
        {
            var result = await _angularUserBL.LoginAngularUser(request);
            return Ok(result);
        }

        [HttpGet("DownloadApplication"), Authorize(Roles = $"{Constants.AngularUserRole}")]
        public async Task<IActionResult> DownloadApplication()
        {
            var result = await _angularUserBL.GetApplication();
            return Ok(result);
        }

        [HttpPost("AddApplication"), Authorize(Roles = $"{Constants.AngularUserRole}")]
        public async Task<IActionResult> AddApplication(IFormFile apk)
        {
            if (apk.Length > 0 && apk.FileName.EndsWith(".apk"))
            {
                using (var ms = new MemoryStream())
                {
                    apk.CopyTo(ms);
                    var apkBytes = ms.ToArray();
                    await _angularUserBL.AddApplication(apkBytes);
                    return Ok(new { Succes = true });
                }
            }
            else
            {
                return BadRequest();
            }
        }
    }
}
