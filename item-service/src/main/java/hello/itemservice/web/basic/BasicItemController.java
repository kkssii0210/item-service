package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemRepositoryV2;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepositoryV2 itemRepositoryV2;
    private final ItemRepository itemRepository;
//    @GetMapping
//    public String items(Model model) {
//        List<Item> items = itemRepositoryV2.findAll();
//        model.addAttribute("items",items);
//        return "basic/items";
//    }
    @GetMapping
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<Item> items = itemRepositoryV2.findAll(pageable);
        model.addAttribute("items",items);
        return "basic/items";
    }
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Optional<Item> item1 = itemRepositoryV2.findById(itemId);
        if (item1.isPresent()) {
            Item item = item1.get();
            model.addAttribute("item", item);
        }
        return "basic/item";
    }
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Optional<Item> item1 = itemRepositoryV2.findById(itemId);
        if (item1.isPresent()) {
            Item item = item1.get();
            model.addAttribute("item", item);
        }
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit")
    @Transactional
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        Item findItem = itemRepositoryV2.findById(itemId).orElseThrow();
        findItem.setItemName(item.getItemName());
        findItem.setPrice(item.getPrice());
        findItem.setQuantity(item.getQuantity());
        return "redirect:/basic/items/{itemId}";
    }
    @PostMapping("/add")
    @Transactional
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepositoryV2.save(item);
        redirectAttributes.addAttribute("itemId",savedItem.getId());
        redirectAttributes.addAttribute("status",true);
        return "redirect:/basic/items/{itemId}";
    }
    @PostMapping("/{itemId}/delete")
    @Transactional
    public String delete(@PathVariable Long itemId){
        itemRepositoryV2.deleteById(itemId);
        return "redirect:/basic/items";
    }
    @PostConstruct
    public void init() {
        for (int i = 0; i < 20; i++) {
            itemRepositoryV2.save(new Item("item"+i,100+i,100));
        }
    }
}
