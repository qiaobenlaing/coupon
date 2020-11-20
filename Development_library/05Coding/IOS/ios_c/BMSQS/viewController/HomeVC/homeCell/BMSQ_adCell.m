//
//  BMSQ_adCell.m
//  BMSQC
//
//  Created by liuqin on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_adCell.h"
#import "UIImageView+WebCache.h"

@interface BMSQ_adCell ()<UIScrollViewDelegate>

@property (nonatomic, strong)UIScrollView *sc;
@property (nonatomic, strong)UIPageControl *pageControl;

@end


@implementation BMSQ_adCell


-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.sc = [[UIScrollView alloc]initWithFrame:CGRectZero];
        self.sc.pagingEnabled = YES;
        self.sc.delegate = self;
        [self addSubview:self.sc];
        
        self.pageControl = [[UIPageControl alloc]initWithFrame:CGRectMake(0, 0, 100, 30)];
        [self addSubview:self.pageControl];
        [self bringSubviewToFront:self.pageControl];
    }
    return self;
}

-(void)setHomeAdCell:(NSArray *)subList height:(float)height{
    self.sc.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, height) ;
    
    if (subList.count>1) {
        self.pageControl.hidden = NO;
        self.pageControl.center = CGPointMake(APP_VIEW_WIDTH/2, height-10);
        self.pageControl.numberOfPages = subList.count;
        self.pageControl.currentPage = 0;
  
    }else{
        self.pageControl.hidden = YES;
    }
    
    
    for (UIView *aboveV in self.sc.subviews) {
        [aboveV removeFromSuperview];
    }
    
    for (int i=0; i<subList.count; i++) {
        NSDictionary *subDic = [subList objectAtIndex:i];
        UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(i*APP_VIEW_WIDTH, 0, APP_VIEW_WIDTH, self.sc.frame.size.height)];
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[subDic objectForKey:@"imgUrl"]]];
        [imageView sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        [self.sc addSubview:imageView];
        imageView.tag = 100+i;
        self.sc.userInteractionEnabled = YES;
        imageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(TapGesture:)];
        [imageView addGestureRecognizer:tap];
    }
    self.sc.contentSize = CGSizeMake(APP_VIEW_WIDTH*subList.count, self.sc.frame.size.height);
    
}
-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    CGFloat pageWidth = scrollView.frame.size.width;
    int page = floor((scrollView.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
    self.pageControl.currentPage = page;
}


-(void)TapGesture:(UITapGestureRecognizer *)tap{
    [self.adDelegate clickAD:(int)tap.view.tag-100];
}
@end
