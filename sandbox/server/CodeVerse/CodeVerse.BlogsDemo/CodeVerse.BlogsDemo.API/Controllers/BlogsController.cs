using CodeVerse.BlogsDemo.API.BL.Interfaces;
using CodeVerse.BlogsDemo.API.Models.Entity;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace CodeVerse.BlogsDemo.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BlogsController : ControllerBase
    {
        private readonly IBlogsBL _blogsBL;

        public BlogsController(IBlogsBL blogsBL)
        {
            _blogsBL = blogsBL;
        }

        [HttpGet("{id:long}")]
        public async Task<IActionResult> Get(long id)
        {
            var result = await _blogsBL.Get(id);
            return Ok(result);
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var result = await _blogsBL.GetAll();
            return Ok(result);
        }

        [HttpPost]
        public async Task<IActionResult> Insert([FromBody] Blog blog)
        {
            var result = await _blogsBL.Insert(blog);
            return Ok(new { Inserted = result });
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> Delete(long id)
        {
            var result = await _blogsBL.Delete(id);
            return Ok(new { Deleted = result });
        }
    }
}
