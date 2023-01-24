using CodeVerse.BlogsDemo.API.BL.Interfaces;
using CodeVerse.BlogsDemo.API.Models.Entity;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace CodeVerse.BlogsDemo.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PostsController : ControllerBase
    {
        private readonly IPostsBL _postsBL;

        public PostsController(IPostsBL postsBL)
        {
            _postsBL = postsBL;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var result = await _postsBL.GetAll();
            return Ok(result);
        }

        [HttpGet("{id:long}")]
        public async Task<IActionResult> Get([FromRoute] long id)
        {
            var result = await _postsBL.Get(id);
            return Ok(result);
        }


        [HttpPost]
        public async Task<IActionResult> Insert([FromBody] Post post)
        {
            var result = await _postsBL.Insert(post);
            return Ok(new { Inserted = result });
        }

        [HttpDelete("{id:long}")]
        public async Task<IActionResult> Delete([FromRoute] long id)
        {
            var result = await _postsBL.Delete(id);
            return Ok(new { Deleted = result });
        }

        [HttpGet("GetByBlogId/{blogId:long}")]
        public async Task<IActionResult> GetPostsByBlogId(long blogId)
        {
            var result = await _postsBL.GetPostsByBlogId(blogId);
            return Ok(result);
        }
    }
}
