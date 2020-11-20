//
//  PhotoScrollView.m
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "PhotoScrollView.h"
#import "UIImageView+AFNetworking.h"
#import "UIImageView+WebCache.h"
#import "UIButtonEx.h"
@implementation PhotoScrollView

-(id)init{
    
    self = [super init];
    if (self) {
        self.frame = [[UIScreen mainScreen]bounds];
        self.backgroundColor = [UIColor blackColor];
        self.pagingEnabled = YES;
        
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesture)];
        [self addGestureRecognizer:tapGesture];
      }
    return self;
}
-(void)setImageView:(NSArray *)imageArray{
    
    
    for (int i = 0; i<imageArray.count; i++) {
        
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    
        [self addSubview:backView];
        
        UIImageView *imageView = [[UIImageView alloc]init];
        [imageView sd_setImageWithURL:[NSURL URLWithString:[imageArray objectAtIndex:i]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        UIImage *image = imageView.image;
        imageView.frame = CGRectMake(0, 0, image.size.width>APP_VIEW_WIDTH?APP_VIEW_WIDTH:image.size.width, image.size.height>APP_VIEW_HEIGHT?APP_VIEW_HEIGHT:image.size.height);
        imageView.center = CGPointMake((APP_VIEW_WIDTH/2), APP_VIEW_HEIGHT/2);
        
        [backView addSubview:imageView];
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80, APP_VIEW_HEIGHT -40, 70, 20)];
        label.backgroundColor = [UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        label.text = [NSString stringWithFormat:@"%d/%d",i+1,self.count];
        [backView addSubview:label];
        
    }
    
    self.contentSize = CGSizeMake(APP_VIEW_WIDTH*imageArray.count, APP_VIEW_HEIGHT);
    
}
-(void)setEnImageArray:(NSArray *)imageArray{
    
    for (int i = 0; i<imageArray.count; i++) {
        NSDictionary *dic = [imageArray objectAtIndex:i];
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
        
        [self addSubview:backView];
        
        UIImageView *imageView = [[UIImageView alloc]init];
        [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"imgUrl"]]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        UIImage *image = imageView.image;
        imageView.frame = CGRectMake(0, 0, image.size.width>APP_VIEW_WIDTH?APP_VIEW_WIDTH:image.size.width, image.size.height>APP_VIEW_HEIGHT?APP_VIEW_HEIGHT:image.size.height);
        imageView.center = CGPointMake((APP_VIEW_WIDTH/2), APP_VIEW_HEIGHT/2);
        
        [backView addSubview:imageView];
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80, APP_VIEW_HEIGHT -40, 70, 20)];
        label.backgroundColor = [UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        label.text = [NSString stringWithFormat:@"%d/%d",i+1,self.count];
        [backView addSubview:label];
        
        
        UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, APP_VIEW_HEIGHT -40, 150, 25)];
        nameLabel.backgroundColor = [UIColor clearColor];
        nameLabel.textColor = [UIColor whiteColor];
        nameLabel.font = [UIFont systemFontOfSize:14.0f];
        nameLabel.text = [dic objectForKey:@"title"];
        [backView addSubview:nameLabel];
        
        UIButtonEx *btn_delete = [UIButtonEx buttonWithType:UIButtonTypeCustom];
        btn_delete.object = dic;
        btn_delete.frame = CGRectMake(15, 40, 50, 44);
        btn_delete.backgroundColor = [UIColor clearColor];
        [btn_delete setTitle:@"删除" forState:UIControlStateNormal];
        [btn_delete setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [btn_delete addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        btn_delete.tag = 1001;
        [backView addSubview:btn_delete];
        
        UIButtonEx *btn_edit = [UIButtonEx buttonWithType:UIButtonTypeCustom];
        btn_edit.object = dic;
        btn_edit.frame = CGRectMake(APP_VIEW_WIDTH-15 -50, 40, 50, 44);
        btn_edit.backgroundColor = [UIColor clearColor];
        [btn_edit setTitle:@"编辑" forState:UIControlStateNormal];
        [btn_edit setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [btn_edit addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        btn_edit.tag = 1002;
        [backView addSubview:btn_edit];
        
        
    }
    
    self.contentSize = CGSizeMake(APP_VIEW_WIDTH*imageArray.count, APP_VIEW_HEIGHT);
    

    
}

-(void)setHononrImageArray:(NSArray *)imageArray string:(NSString *)string
{
    for (int i = 0; i<imageArray.count; i++) {
        NSDictionary *dic = [imageArray objectAtIndex:i];
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
        
        [self addSubview:backView];
        
        UIImageView *imageView = [[UIImageView alloc]init];
        if ([string isEqualToString:@"workUrl"]) {
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"workUrl"]]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        }else if ([string isEqualToString:@"starImgUrl"]){
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"starImgUrl"]]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        }else if ([string isEqualToString:@"honorUrl"]){
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"honorUrl"]]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        }
        
        UIImage *image = imageView.image;
        imageView.frame = CGRectMake(0, 0, image.size.width>APP_VIEW_WIDTH?APP_VIEW_WIDTH:image.size.width, image.size.height>APP_VIEW_HEIGHT?APP_VIEW_HEIGHT:image.size.height);
        imageView.center = CGPointMake((APP_VIEW_WIDTH/2), APP_VIEW_HEIGHT/2);
        
        [backView addSubview:imageView];
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80, APP_VIEW_HEIGHT -40, 70, 20)];
        label.backgroundColor = [UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        label.text = [NSString stringWithFormat:@"%d/%d",i+1,self.count];
        [backView addSubview:label];
        
        
        UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, APP_VIEW_HEIGHT -40, 150, 25)];
        nameLabel.backgroundColor = [UIColor clearColor];
        nameLabel.textColor = [UIColor whiteColor];
        nameLabel.font = [UIFont systemFontOfSize:14.0f];
        nameLabel.text = [dic objectForKey:@"title"];
        [backView addSubview:nameLabel];
        
        
        
    }
    
    self.contentSize = CGSizeMake(APP_VIEW_WIDTH*imageArray.count, APP_VIEW_HEIGHT);
    
    

}

-(void)setParentImageArray:(NSArray *)imageArray
{
    
    for (int i = 0; i<imageArray.count; i++) {
        NSDictionary *dic = [imageArray objectAtIndex:i];
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
        
        [self addSubview:backView];
        
        UIImageView *imageView = [[UIImageView alloc]init];
        [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"remarkImgUrl"]]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        UIImage *image = imageView.image;
        imageView.frame = CGRectMake(0, 0, image.size.width>APP_VIEW_WIDTH?APP_VIEW_WIDTH:image.size.width, image.size.height>APP_VIEW_HEIGHT?APP_VIEW_HEIGHT:image.size.height);
        imageView.center = CGPointMake((APP_VIEW_WIDTH/2), APP_VIEW_HEIGHT/2);
        
        [backView addSubview:imageView];
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80, APP_VIEW_HEIGHT -40, 70, 20)];
        label.backgroundColor = [UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        label.text = [NSString stringWithFormat:@"%d/%d",i+1,self.count];
        [backView addSubview:label];
        
        
        UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, APP_VIEW_HEIGHT -40, 150, 25)];
        nameLabel.backgroundColor = [UIColor clearColor];
        nameLabel.textColor = [UIColor whiteColor];
        nameLabel.font = [UIFont systemFontOfSize:14.0f];
        nameLabel.text = [dic objectForKey:@"title"];
        [backView addSubview:nameLabel];
        
        
        
    }
    
    self.contentSize = CGSizeMake(APP_VIEW_WIDTH*imageArray.count, APP_VIEW_HEIGHT);
    

    
}





-(void)setImageDicView:(NSArray *)imageArray{
    
    
    for (int i = 0; i<imageArray.count; i++) {
        NSDictionary *dic = [imageArray objectAtIndex:i];
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
        
        [self addSubview:backView];
        
        UIImageView *imageView = [[UIImageView alloc]init];
        [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"url"]]] placeholderImage:[UIImage imageNamed:@"tab_mine"]];
        UIImage *image = imageView.image;
        imageView.frame = CGRectMake(0, 0, image.size.width>APP_VIEW_WIDTH?APP_VIEW_WIDTH:image.size.width, image.size.height>APP_VIEW_HEIGHT?APP_VIEW_HEIGHT:image.size.height);
        imageView.center = CGPointMake((APP_VIEW_WIDTH/2), APP_VIEW_HEIGHT/2);
        
        [backView addSubview:imageView];
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-80, APP_VIEW_HEIGHT -40, 70, 20)];
        label.backgroundColor = [UIColor clearColor];
        label.textColor = [UIColor whiteColor];
        label.text = [NSString stringWithFormat:@"%d/%d",i+1,self.count];
        [backView addSubview:label];
        
        
        UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, APP_VIEW_HEIGHT -60, 150, 25)];
        nameLabel.backgroundColor = [UIColor clearColor];
        nameLabel.textColor = [UIColor whiteColor];
        nameLabel.font = [UIFont systemFontOfSize:14.0f];
        nameLabel.text = [dic objectForKey:@"title"];
        [backView addSubview:nameLabel];
        
        
        UILabel *priceLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, APP_VIEW_HEIGHT -60+25, 150, 25)];
        priceLabel.backgroundColor = [UIColor clearColor];
        priceLabel.textColor = [UIColor whiteColor];
        priceLabel.font = [UIFont systemFontOfSize:13.0f];

        priceLabel.text = [NSString stringWithFormat:@"￥%@",[dic objectForKey:@"price"]];
        [backView addSubview:priceLabel];
        
        UIButtonEx *btn_delete = [UIButtonEx buttonWithType:UIButtonTypeCustom];
        btn_delete.object = dic;
        btn_delete.frame = CGRectMake(15, 40, 50, 44);
        btn_delete.backgroundColor = [UIColor clearColor];
        [btn_delete setTitle:@"删除" forState:UIControlStateNormal];
        [btn_delete setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [btn_delete addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        btn_delete.tag = 1001;
        [backView addSubview:btn_delete];
        
        UIButtonEx *btn_edit = [UIButtonEx buttonWithType:UIButtonTypeCustom];
        btn_edit.object = dic;
        btn_edit.frame = CGRectMake(APP_VIEW_WIDTH-15 -50, 40, 50, 44);
        btn_edit.backgroundColor = [UIColor clearColor];
        [btn_edit setTitle:@"编辑" forState:UIControlStateNormal];
        [btn_edit setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        [btn_edit addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        btn_edit.tag = 1002;
        [backView addSubview:btn_edit];
        
        
        
    }
    
    self.contentSize = CGSizeMake(APP_VIEW_WIDTH*imageArray.count, APP_VIEW_HEIGHT);
    
    
}
-(void)setImage:(int)i{
    self.contentOffset = CGPointMake(i*APP_VIEW_WIDTH, 0);
    
    
     
}
-(void)tapGesture{
    
    if(self.removeSC){
        self.removeSC();
    }
    
}

- (void)btnAction:(UIButtonEx *)button {
    
    if (self.delegate!=nil) {
        [self.delegate PhotoScrollbtnAction:button];
        [self removeFromSuperview];
    }
//    if (button.tag == 1001) {
//        NSLog(@"删除%@",button.object);
//      
//    } else if (button.tag == 1002) {
//        NSLog(@"编辑%@",button.object);
//        
//    }
//    
    
}




@end
