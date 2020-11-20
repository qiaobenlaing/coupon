//
//  BMSQ_PhotoViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PhotoViewController.h"

#import "SVProgressHUD.h"


#import "ProctionCollectionViewCell.h"
#import "ENCollectionViewCell.h"

#import "BMSQ_SubAlbumPhotoViewController.h"
#import "MobClick.h"
#import "MJRefresh.h"
#import "PhotoScrollView.h"
@interface BMSQ_PhotoViewController ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>


@property(nonatomic, strong)NSMutableArray *ProductArray;
@property(nonatomic, strong)NSMutableArray *environmentArray;
@property(nonatomic, strong)UICollectionView *collectionView;


@property(nonatomic, assign)int proPage;  //产品相册页码
@property(nonatomic, assign)int enPage;   //环境相册页码

@property(nonatomic, assign)int currenPage;  //当前相册  1 产品； 2 环境
@property(nonatomic, strong)UIImageView *nodataImage;

@end

@implementation BMSQ_PhotoViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"PhotoView"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"PhotoView"];
}


- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"商家相册"];
    
    self.nodataImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    [self.nodataImage setImage:[UIImage imageNamed:@"noDataImage"]];
    [self.view addSubview:self.nodataImage];
    

    
    NSArray *segmentedArray = [[NSArray alloc]initWithObjects:@"产品",@"环境",nil];
    //初始化UISegmentedControl
    UISegmentedControl *segmentedTemp = [[UISegmentedControl alloc]initWithItems:segmentedArray];
    segmentedTemp.frame = CGRectMake(0, 0, 160.0, 35.0);
    segmentedTemp.center = CGPointMake(APP_VIEW_WIDTH/2, 100);
    segmentedTemp.tintColor= APP_NAVCOLOR;
    
    UIColor *greenColor = APP_TEXTCOLOR;
    NSDictionary *colorAttr = [NSDictionary dictionaryWithObject:greenColor forKey:NSForegroundColorAttributeName];
    [segmentedTemp setTitleTextAttributes:colorAttr forState:UIControlStateNormal];
    
    UIColor *redColor = [UIColor whiteColor];
    NSDictionary *selecolorAttr = [NSDictionary dictionaryWithObject:redColor forKey:NSForegroundColorAttributeName];
    [segmentedTemp setTitleTextAttributes:selecolorAttr forState:UIControlStateSelected];
    
    
    segmentedTemp.selectedSegmentIndex = 1;
    [self.view addSubview:segmentedTemp];
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(10,  100+35/2, APP_VIEW_WIDTH-20, 0.5)];
    lineView.backgroundColor = APP_NAVCOLOR;
    lineView.alpha = 0.613;
    [self.view addSubview:lineView];
    
    [segmentedTemp addTarget:self action:@selector(didClicksegmentedControlAction:) forControlEvents:UIControlEventValueChanged];
    
    
    
    
    self.ProductArray = [[NSMutableArray alloc]init];
    self.environmentArray = [[NSMutableArray alloc]init];
    self.proPage = 1;
    self.enPage = 1;
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc]init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    self.collectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(0, lineView.frame.origin.y+20, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-APP_VIEW_ORIGIN_Y) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor clearColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    [self.view addSubview:self.collectionView];
    
    [self.collectionView addFooterWithTarget:self action:@selector(footerRefreshing)];
    
    
    [self.collectionView registerClass:[ProctionCollectionViewCell class] forCellWithReuseIdentifier:@"ProctionCollectionViewCell"];
    [self.collectionView registerClass:[ENCollectionViewCell class] forCellWithReuseIdentifier:@"enCollectionViewCell"];
    
    self.currenPage = 2;
    
    
    
    
    
    [self loadenPhoto];

}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{

    if (self.currenPage ==1) {
        if(self.ProductArray.count>1){
            self.nodataImage.hidden = YES;
        }else{
            self.nodataImage.hidden = NO;
 
        }
        
        return self.ProductArray.count;
    }else{
        
        if(self.environmentArray.count>1){
            self.nodataImage.hidden = YES;
        }else{
            self.nodataImage.hidden = NO;
            
        }

        return self.environmentArray.count;
    }
    
    
}


-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.currenPage ==1) {
        static NSString *identifer = @"ProctionCollectionViewCell" ;
        ProctionCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
        
        if(cell == nil){
            cell = [[ProctionCollectionViewCell alloc]init];
        }
        [cell setCollectionCell:[self.ProductArray objectAtIndex:indexPath.row] row:(int)indexPath.row];
        
        
        return cell;
    }else{
        
        static NSString *identifer = @"enCollectionViewCell" ;
        ENCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
        
        if(cell == nil){
            cell = [[ENCollectionViewCell alloc]init];
        }
        
        
        [cell setEnCell:[self.environmentArray objectAtIndex:indexPath.row] row:(int)indexPath.row];
        return cell;

        
    }
   
    
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    if(self.currenPage == 1){
        return CGSizeMake(APP_VIEW_WIDTH/2, APP_VIEW_WIDTH/2-20);
  
    }else{
        return CGSizeMake(APP_VIEW_WIDTH/2, APP_VIEW_WIDTH/2+20);
 
    }
    
}
//
-(UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    
    return UIEdgeInsetsMake(0,0,0, 0);
    
}
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}
-(CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}


-(BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    return  YES;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    
    
    switch (self.currenPage) {
        case 1:
        {
            NSDictionary *dic = [self.ProductArray objectAtIndex:indexPath.row];
            BMSQ_SubAlbumPhotoViewController *vc = [[BMSQ_SubAlbumPhotoViewController alloc]init];
            vc.myTitle = [dic objectForKey:@"name"];
            vc.proDic = dic;
            [self.navigationController pushViewController:vc animated:YES];
            
         }
            break;
        case 2:{
            
            PhotoScrollView *sc = [[PhotoScrollView alloc]init];
            UIApplication *app = [UIApplication sharedApplication];
            UIWindow *window = app.keyWindow;
            __block PhotoScrollView *_weakPhoto=sc;
            sc.removeSC = ^{
                [_weakPhoto removeFromSuperview];
                
            };
            
            sc.count = (int)self.environmentArray.count;
            [sc setEnImageArray:self.environmentArray];
            [sc setImage:(int)indexPath.row];
            [window addSubview:sc];
            
            
            
            
        }
            break;
        default:
            break;
    }
   
}





#pragma mark --click--
-(void)didClicksegmentedControlAction:(id)sender{
    
    UISegmentedControl *control = (UISegmentedControl *)sender;
    int tag = (int)control.selectedSegmentIndex;
    switch (tag) {
        case 0:   //产品
        {
            self.currenPage = 1;
            [self.collectionView reloadData];
            if (self.ProductArray.count ==0) {
                [self loadProdctionPhoto];
            }
            
        }
            break;
        case 1:  //环境
        {
            self.currenPage = 2;
            [self.collectionView reloadData];
            if (self.environmentArray.count ==0) {
                [self loadenPhoto];
            }

        }
            break;
            
        default:
            break;
    }
    
    
}



-(void)footerRefreshing{
    
//    [self.collectionView footerBeginRefreshing];

    if (self.currenPage ==1) {
        [self loadProdctionPhoto];
        
    }else{
        [self loadenPhoto];
    }
    
}

-(void)loadProdctionPhoto{
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.proPage] forKey:@"page"];

    
    NSString* vcode = [gloabFunction getSign:@"cGetShopProductAlbum" strParams:self.shopCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"cGetShopProductAlbum" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        self.proPage += 1;
        if ([responseObject objectForKey:@"subAlbumList"]) {
            [wself.ProductArray addObjectsFromArray:[responseObject objectForKey:@"subAlbumList"]];

        }
        [wself.collectionView reloadData];
        
        if (wself.ProductArray.count ==0) {
            wself.nodataImage.hidden = NO;
        }else{
            wself.nodataImage.hidden = YES;

        }
        
        [SVProgressHUD dismiss];

        [self.collectionView footerEndRefreshing];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        [self.collectionView footerEndRefreshing];

        
    }];

    
}

//环境相册
-(void)loadenPhoto{
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.enPage] forKey:@"page"];
    
    
    NSString* vcode = [gloabFunction getSign:@"cGetShopDecoration" strParams:self.shopCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"cGetShopDecoration" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        self.enPage += 1;
        if ([responseObject objectForKey:@"shopDecoList"]) {
            
            [wself.environmentArray addObjectsFromArray: [responseObject objectForKey:@"shopDecoList"]];
            }
        [wself.collectionView reloadData];
        
        [SVProgressHUD dismiss];
        
        if (wself.environmentArray.count ==0) {
            wself.nodataImage.hidden = NO;
        }else{
            wself.nodataImage.hidden = YES;
            
        }
        
        [self.collectionView footerEndRefreshing];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
        [self.collectionView footerEndRefreshing];

    }];
    
    
}

@end
