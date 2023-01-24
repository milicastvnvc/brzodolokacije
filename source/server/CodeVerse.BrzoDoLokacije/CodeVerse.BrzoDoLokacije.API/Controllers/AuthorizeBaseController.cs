using CodeVerse.BrzoDoLokacije.Common.Constants;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace CodeVerse.BrzoDoLokacije.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize(Roles = $"{Constants.UserRole}")]
    public class AuthorizeBaseController : ControllerBase
    {
    }
}
